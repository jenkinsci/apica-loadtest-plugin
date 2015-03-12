/*
 * The MIT License
 *
 * Copyright 2015 andras.nemes.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.apica.apicaloadtest.model;

import com.apica.apicaloadtest.environment.LoadtestEnvironment;
import com.apica.apicaloadtest.environment.LoadtestEnvironmentFactory;
import com.apica.apicaloadtest.infrastructure.JobParamValidatorService;
import com.apica.apicaloadtest.infrastructure.PresetResponse;
import com.apica.apicaloadtest.infrastructure.RunnableFileResponse;
import com.apica.apicaloadtest.infrastructure.ServerSideLtpApiWebService;
import com.apica.apicaloadtest.jobexecution.validation.JobParamsValidationResult;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 *
 * @author andras.nemes
 */
public class LoadtestBuilderModel extends AbstractDescribableImpl<LoadtestBuilderModel>
{

    private final String environmentShortName;
    private final String authToken;
    private final String presetName;
    private final String loadtestScenario;
    private final List<LoadtestBuilderThresholdModel> loadtestThresholdParameters;
    private final List<Threshold> thresholds;
    private final static JobParamValidatorService validatorService = new JobParamValidatorService();

    @DataBoundConstructor
    public LoadtestBuilderModel(String environmentShortName, String authToken, String presetName, String loadtestScenario, List<LoadtestBuilderThresholdModel> loadtestThresholdParameters)
    {
        this.environmentShortName = environmentShortName;
        this.authToken = authToken;
        this.presetName = presetName;
        this.loadtestScenario = loadtestScenario;
        this.loadtestThresholdParameters = loadtestThresholdParameters;
        thresholds = new ArrayList<>();
        for (LoadtestBuilderThresholdModel loadtestThresholdParameter : loadtestThresholdParameters)
        {
            Threshold t = new Threshold(loadtestThresholdParameter.getLoadtestThresholdMetric(),
                    loadtestThresholdParameter.getThresholdDirection(), 
                    loadtestThresholdParameter.getNumericValue());
            thresholds.add(t);
        }
    }

    public List<Threshold> getThresholds()
    {
        return thresholds;
    }

    public String getEnvironmentShortName()
    {
        return environmentShortName;
    }

    public String getAuthToken()
    {
        return authToken;
    }

    public String getPresetName()
    {
        return presetName;
    }

    public String getLoadtestScenario()
    {
        return loadtestScenario;
    }

    public List<LoadtestBuilderThresholdModel> getLoadtestThresholdParameters()
    {
        return loadtestThresholdParameters;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<LoadtestBuilderModel>
    {

        @Override
        public String getDisplayName()
        {
            return "Loadtest environment";
        }

        public List<LoadtestEnvironment> getEnvironments()
        {
            return LoadtestEnvironmentFactory.getLoadtestEnvironments();
        }

        public FormValidation doCheckAuthToken(@QueryParameter String value)
                throws IOException, ServletException
        {
            if (!validatorService.paramValueOkClientSide(value))
            {
                return FormValidation.error("Please set a valid auth token.");
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckPresetName(@QueryParameter String value)
        {
            if (!validatorService.paramValueOkClientSide(value))
            {
                return FormValidation.error("Please set a preset name.");
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckLoadtestScenario(@QueryParameter String value)
        {
            if (!validatorService.paramValueOkClientSide(value))
            {
                return FormValidation.error("Please set a loadtest scenario name.");
            }
            else if (!validatorService.scenarioNameOkClientSide(value))
            {
                return FormValidation.error("Load test file name must be either a .class or .zip file.");
            }
            return FormValidation.ok();
        }

        public FormValidation doTestSettings(
                @QueryParameter("environmentShortName") final String environmentShortName,
                @QueryParameter("authToken") final String authToken,
                @QueryParameter("presetName") final String presetName,
                @QueryParameter("loadtestScenario") final String loadtestScenario) throws IOException, ServletException
        {
            LoadtestEnvironment le = LoadtestEnvironmentFactory.getLoadtestEnvironment(environmentShortName);
            JobParamsValidationResult validateJobParameters = validatorService.validateJobParameters(authToken, presetName, loadtestScenario, le);
            
            if (!isNullOrEmpty(validateJobParameters.getAuthTokenException()))
            {
                return FormValidation.error(validateJobParameters.getAuthTokenException());
            }

            if (!isNullOrEmpty(validateJobParameters.getPresetNameException()))
            {
                return FormValidation.error(validateJobParameters.getPresetNameException());
            }

            if (!isNullOrEmpty(validateJobParameters.getScenarioFileException()))
            {
                return FormValidation.error(validateJobParameters.getScenarioFileException());
            }
            return FormValidation.ok("All set");
        }

        private boolean isNullOrEmpty(String s)
        {
            if (s == null)
            {
                return true;
            }
            if (s.length() == 0)
            {
                return true;
            }
            if (s.trim().equals(""))
            {
                return true;
            }
            if (s.equals(""))
            {
                return true;
            }
            return false;
        }
    }
}
