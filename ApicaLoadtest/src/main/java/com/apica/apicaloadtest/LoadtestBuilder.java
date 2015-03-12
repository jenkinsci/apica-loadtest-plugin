package com.apica.apicaloadtest;

import com.apica.apicaloadtest.environment.LoadtestEnvironment;
import com.apica.apicaloadtest.environment.LoadtestEnvironmentFactory;
import com.apica.apicaloadtest.jobexecution.validation.JobParamsValidationResult;
import com.apica.apicaloadtest.jobexecution.LoadtestJobSummaryResponse;
import com.apica.apicaloadtest.model.LoadtestBuilderModel;
import com.google.gson.Gson;
import hudson.Launcher;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import java.io.PrintStream;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import java.util.List;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked and a new
 * {@link LoadtestBuilder} is created. The created instance is persisted to the
 * project configuration XML by using XStream, so this allows you to use
 * instance fields (like {@link #environment}) to remember the configuration.
 *
 * <p>
 * When a build is performed, the
 * {@link #perform(AbstractBuild, Launcher, BuildListener)} method will be
 * invoked.
 *
 * @author Kohsuke Kawaguchi
 */
public class LoadtestBuilder extends Builder
{
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
        PrintStream logger = listener.getLogger();
        Gson gson = new Gson();
        logger.println("Apica Loadtest starting...");
        
        logger.print(gson.toJson(this.loadtestBuilderModel));
        //listener.finished(Result.SUCCESS);
        //listener.finished(Result.FAILURE);
        return true;
    }
    
    private JobParamsValidationResult validateJobParams()
    {
        JobParamsValidationResult res = new JobParamsValidationResult();
        StringBuilder sb = new StringBuilder();
        String loadtestPresetName = this.loadtestBuilderModel.getPresetName();
        String loadtestFileName = this.loadtestBuilderModel.getLoadtestScenario();
        String authToken = this.loadtestBuilderModel.getAuthToken();
        res.setAllParamsPresent(true);
        return res;
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
