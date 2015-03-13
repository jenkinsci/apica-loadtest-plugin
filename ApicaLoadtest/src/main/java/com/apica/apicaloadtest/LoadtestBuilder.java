package com.apica.apicaloadtest;

import com.apica.apicaloadtest.environment.LoadtestEnvironment;
import com.apica.apicaloadtest.environment.LoadtestEnvironmentFactory;
import com.apica.apicaloadtest.infrastructure.JobParamValidatorService;
import com.apica.apicaloadtest.infrastructure.JobStatusRequest;
import com.apica.apicaloadtest.infrastructure.ServerSideLtpApiWebService;
import com.apica.apicaloadtest.jobexecution.requestresponse.JobStatusResponse;
import com.apica.apicaloadtest.jobexecution.requestresponse.LoadtestJobSummaryRequest;
import com.apica.apicaloadtest.jobexecution.requestresponse.TransmitJobRequestArgs;
import com.apica.apicaloadtest.jobexecution.validation.JobParamsValidationResult;
import com.apica.apicaloadtest.jobexecution.requestresponse.LoadtestJobSummaryResponse;
import com.apica.apicaloadtest.jobexecution.requestresponse.StartJobByPresetResponse;
import com.apica.apicaloadtest.model.LoadtestBuilderModel;
import com.apica.apicaloadtest.model.LoadtestBuilderThresholdModel;
import com.apica.apicaloadtest.model.Threshold;
import com.apica.apicaloadtest.model.ThresholdEvaluationResult;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import java.util.List;
import jenkins.model.ArtifactManager;
import jenkins.util.VirtualFile;

public class LoadtestBuilder extends Builder
{
    private static final String artifactsDirectoryName = "archive";
    public static final String artifactsResourceName = "artifact";
    private final LoadtestBuilderModel loadtestBuilderModel;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public LoadtestBuilder(LoadtestBuilderModel loadtestBuilderModel)
    {
        this.loadtestBuilderModel = loadtestBuilderModel;
    }

    public LoadtestBuilderModel getLoadtestBuilderModel()
    {
        return loadtestBuilderModel;
    }

    @Override
    public DescriptorImpl getDescriptor()
    {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener)
    {
        
        List<Threshold> thresholds = new ArrayList<>();
        for (LoadtestBuilderThresholdModel loadtestThresholdParameter : loadtestBuilderModel.getLoadtestThresholdParameters())
        {
            Threshold t = new Threshold(loadtestThresholdParameter.getLoadtestThresholdMetric(),
                    loadtestThresholdParameter.getThresholdDirection(),
                    loadtestThresholdParameter.getNumericValue());
            thresholds.add(t);
        }
        PrintStream logger = listener.getLogger();
        logger.println("Apica Loadtest starting...");
        JobParamsValidationResult validationResult = validateJobParams();
        LoadtestEnvironment le = LoadtestEnvironmentFactory.getLoadtestEnvironment(loadtestBuilderModel.getEnvironmentShortName());
        if (!validationResult.isAllParamsPresent())
        {
            logger.println(validationResult.getExceptionSummary());
            listener.finished(Result.FAILURE);
            return false;
        }
        logger.println("Load test preset name: ".concat(loadtestBuilderModel.getPresetName()));
        logger.println("Load test file name: ".concat(loadtestBuilderModel.getLoadtestScenario()));
        logger.println("Load test environment: ".concat(le.getDisplayName()));
        if (!thresholds.isEmpty())
        {
            logger.println("Threshold values: \r\n");
            for (Threshold threshold : thresholds)
            {
                logger.println(threshold);
            }
        }
        
        logger.println("Exploring AbstractBuild object...");
        ArtifactManager artifactManager = build.getArtifactManager();
        VirtualFile root = artifactManager.root();
        logger.println("Artifact manager root: " + root.getName());
        
        FilePath moduleRoot = build.getModuleRoot();
        logger.println("Module root: " + moduleRoot.getName());
        
        return true;
        //return runLoadtestJob(logger, thresholds);
    }

    private boolean runLoadtestJob(PrintStream logger, List<Threshold> thresholds)
    {
        LoadtestEnvironment le = LoadtestEnvironmentFactory.getLoadtestEnvironment(loadtestBuilderModel.getEnvironmentShortName());
        ServerSideLtpApiWebService loadtestService = new ServerSideLtpApiWebService(le);;
        logger.println("Attempting to initiate load test...");
        String loadtestPresetName = loadtestBuilderModel.getPresetName();
        String loadtestFileName = loadtestBuilderModel.getLoadtestScenario();
        String authToken = loadtestBuilderModel.getAuthToken();
        boolean success = true;
        try
        {
            TransmitJobRequestArgs transmitJobRequestArgs = new TransmitJobRequestArgs();
            transmitJobRequestArgs.setFileName(loadtestFileName);
            transmitJobRequestArgs.setAuthToken(authToken);
            transmitJobRequestArgs.setPresetName(loadtestPresetName);
            StartJobByPresetResponse startByPresetResponse = loadtestService.transmitJob(transmitJobRequestArgs);
            if (startByPresetResponse.getJobId() > 0)
            {
                int jobId = startByPresetResponse.getJobId();
                logger.println("Successfully inserted job. Job id: ".concat(Integer.toString(jobId)));
                JobStatusRequest jobStatusRequest = new JobStatusRequest();
                jobStatusRequest.setJobId(jobId);
                jobStatusRequest.setAuthToken(authToken);
                JobStatusResponse jobStatus = loadtestService.checkJobStatus(jobStatusRequest);
                logJobStatus(jobStatus, logger);
                while (!jobStatus.isJobCompleted())
                {
                    jobStatus = loadtestService.checkJobStatus(jobStatusRequest);
                    logJobStatus(jobStatus, logger);
                    try
                    {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex)
                    {
                    }
                }
                if (jobStatus.isCompleted() && !jobStatus.isJobFaulted())
                {
                    logger.println("Job has finished normally. Will now attempt to retrieve some job statistics.");
                    LoadtestJobSummaryRequest summaryRequest = new LoadtestJobSummaryRequest();
                    summaryRequest.setJobId(jobId);
                    summaryRequest.setAuthToken(authToken);
                    LoadtestJobSummaryResponse summaryResponse = loadtestService.getJobSummaryResponse(summaryRequest);

                    logJobSummary(summaryResponse, logger);
                    if (!thresholds.isEmpty())
                    {
                        boolean atLeastOneThresholdBroken = false;
                        logger.println("Evaluating threshold values...");
                        for (Threshold threshold : thresholds)
                        {
                            ThresholdEvaluationResult evaluate = threshold.evaluate(summaryResponse.getPerformanceSummary());
                            logger.println(evaluate.getReport());
                            if (evaluate.isThresholdBroken())
                            {
                                atLeastOneThresholdBroken = true;
                            }
                        }
                        if (atLeastOneThresholdBroken)
                        {
                            logger.println("Job has finished with at least one broken threshold.");
                            success = false;
                        }
                    }
                } else if (jobStatus.isJobFaulted())
                {
                    logger.println("Job has finished with an error: ".concat(jobStatus.getStatusMessage()));
                    success = false;
                }
            } else
            {
                logger.println(startByPresetResponse.getException());
                success = false;
            }
        } catch (URISyntaxException ex)
        {
            logger.println(ex.getMessage());
            success = false;
        }
        return success;
    }

    private JobParamsValidationResult validateJobParams()
    {
        String loadtestPresetName = this.loadtestBuilderModel.getPresetName();
        String loadtestFileName = this.loadtestBuilderModel.getLoadtestScenario();
        String authToken = this.loadtestBuilderModel.getAuthToken();
        LoadtestEnvironment le = LoadtestEnvironmentFactory.getLoadtestEnvironment(loadtestBuilderModel.getEnvironmentShortName());
        JobParamValidatorService service = new JobParamValidatorService();
        return service.validateJobParameters(authToken, loadtestPresetName, loadtestFileName, le);
    }

    private void logJobStatus(JobStatusResponse jobStatusResponse, PrintStream logger)
    {
        if (jobStatusResponse.getException().equals(""))
        {
            logger.println(jobStatusResponse.toString());
        } else
        {
            logger.println("Exception when retrieving job status: " + jobStatusResponse.getException());
        }
    }

    private void logJobSummary(LoadtestJobSummaryResponse jobSummaryResponse, PrintStream logger)
    {
        if (jobSummaryResponse.getException().equals(""))
        {
            logger.println(jobSummaryResponse.toString());
        } else
        {
            logger.println("Exception when retrieving job summary statistics: " + jobSummaryResponse.getException());
        }
    }

    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder>
    {

        public DescriptorImpl()
        {
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass)
        {
            return true;
        }

        @Override
        public String getDisplayName()
        {
            return "Apica Loadtest";
        }

        public List<LoadtestEnvironment> getEnvironments()
        {
            return LoadtestEnvironmentFactory.getLoadtestEnvironments();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException
        {
            save();
            return super.configure(req, formData);
        }

    }
}
