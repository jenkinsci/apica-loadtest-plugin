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
package com.apica.apicaloadtest.infrastructure;

import com.apica.apicaloadtest.jobexecution.requestresponse.PresetResponse;
import com.apica.apicaloadtest.jobexecution.requestresponse.RunnableFileResponse;
import com.apica.apicaloadtest.environment.LoadtestEnvironment;
import com.apica.apicaloadtest.jobexecution.validation.JobParamsValidationResult;
import com.apica.apicaloadtest.utils.Utils;

/**
 *
 * @author andras.nemes
 */
public class JobParamValidatorService
{

    public JobParamsValidationResult validateJobParameters(String authToken, String presetName,
            String loadtestScenario, LoadtestEnvironment le)
    {
        JobParamsValidationResult res = new JobParamsValidationResult();
        StringBuilder summaryBuilder = new StringBuilder();
        String NL = System.getProperty("line.separator");
        res.setAllParamsPresent(true);

        if (Utils.isNullOrEmpty(authToken))
        {
            res.setAuthTokenException("Auth token cannot be empty");
            summaryBuilder.append("Unable to retrieve the LTP auth token. Please re-enter it on the setup page. ").append(NL);
            res.setAllParamsPresent(false);
        }
        if (Utils.isNullOrEmpty(presetName))
        {
            res.setPresetNameException("Preset name cannot be empty");
            summaryBuilder.append("Unable to retrieve the loadtest preset name. Please re-enter it on the setup page. ").append(NL);
            res.setAllParamsPresent(false);
        }
        if (Utils.isNullOrEmpty(loadtestScenario))
        {
            res.setPresetNameException("Loadtest scenario cannot be empty");
            summaryBuilder.append("Unable to retrieve the loadtest file name. Please re-enter it on the setup page. ").append(NL);
            res.setAllParamsPresent(false);
        }
        if (!fileIsClass(loadtestScenario) && !fileIsZip(loadtestScenario))
        {
            res.setPresetNameException("Load test file name must be either a .class or .zip file.");
            summaryBuilder.append("Unable to resolve the loadtest file name. Load test file name must be either a .class or .zip file. ").append(NL);
            res.setAllParamsPresent(false);
        }
        ServerSideLtpApiWebService serverSideService = new ServerSideLtpApiWebService(le);
        PresetResponse presetResponse = serverSideService.checkPreset(authToken, presetName);
        if (!presetResponse.isPresetExists())
        {
            summaryBuilder.append("Cannot find this preset: ").append(presetName).append(". ").append(NL);
            res.setAllParamsPresent(false);
            if (presetResponse.getException() != null && !presetResponse.getException().equals(""))
            {
                res.setPresetNameException("Exception while checking preset: ".concat(presetResponse.getException()));
            } else
            {
                res.setPresetNameException("No such preset found: ".concat(presetName));
            }
        } else //validate test instance id
        {
            if (presetResponse.getTestInstanceId() < 1)
            {
                res.setPresetNameException("The preset is not linked to a valid test instance. Please check in LTP if you have selected an existing test instance for the preset.");
                summaryBuilder.append("The preset is not linked to a valid test instance. Please check in LTP if you have selected an existing test instance for the preset").append(NL);
                res.setAllParamsPresent(false);
            }
        }

        RunnableFileResponse runnableFileResponse = serverSideService.checkRunnableFile(authToken, loadtestScenario);
        if (!runnableFileResponse.isFileExists())
        {
            if (runnableFileResponse.getException() != null && !runnableFileResponse.getException().equals(""))
            {
                res.setScenarioFileException("Exception while checking load test file: ".concat(runnableFileResponse.getException()));
            } else
            {
                res.setScenarioFileException("No such load test file found: ".concat(loadtestScenario));
            }
        }

        res.setExceptionMessage(summaryBuilder.toString());
        return res;
    }

    public boolean paramValueOkClientSide(String paramValue)
    {
        if (Utils.isNullOrEmpty(paramValue))
        {
            return false;
        }
        return true;
    }

    public boolean scenarioNameOkClientSide(String scenarioFileName)
    {
        if (Utils.isNullOrEmpty(scenarioFileName))
        {
            return false;
        } else if (!fileIsClass(scenarioFileName) && !fileIsZip(scenarioFileName))
        {
            return false;
        }
        return true;
    }

    private boolean fileIsZip(String fileName)
    {
        return fileName.endsWith(".zip");
    }

    private boolean fileIsClass(String fileName)
    {
        return fileName.endsWith(".class");
    }

}
