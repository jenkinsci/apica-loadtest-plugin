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
package com.apica.apicaloadtest.jobexecution.validation;

/**
 *
 * @author andras.nemes
 */
public class JobParamsValidationResult
{
    private boolean allParamsPresent;
    private int presetTestInstanceId;
    private String exceptionSummary;
    private String authTokenException;
    private String scenarioFileException;
    private String presetNameException;
    private String apiBaseUrlNameException;
    private String webBaseUrlNameException;

    public String getWebBaseUrlNameException()
    {
        return webBaseUrlNameException;
    }

    public void setWebBaseUrlNameException(String webBaseUrlNameException)
    {
        this.webBaseUrlNameException = webBaseUrlNameException;
    }
    
    public String getApiBaseUrlNameException()
    {
        return apiBaseUrlNameException;
    }

    public void setApiBaseUrlNameException(String apiBaseUrlNameException)
    {
        this.apiBaseUrlNameException = apiBaseUrlNameException;
    }
    
    public int getPresetTestInstanceId()
    {
        return presetTestInstanceId;
    }

    public void setPresetTestInstanceId(int presetTestInstanceId)
    {
        this.presetTestInstanceId = presetTestInstanceId;
    }
    
    public String getAuthTokenException()
    {
        return authTokenException;
    }

    public void setAuthTokenException(String authTokenException)
    {
        this.authTokenException = authTokenException;
    }

    public String getScenarioFileException()
    {
        return scenarioFileException;
    }

    public void setScenarioFileException(String scenarioFileException)
    {
        this.scenarioFileException = scenarioFileException;
    }

    public String getPresetNameException()
    {
        return presetNameException;
    }

    public void setPresetNameException(String presetNameException)
    {
        this.presetNameException = presetNameException;
    }
    
    public boolean isAllParamsPresent()
    {
        return allParamsPresent;
    }

    public void setAllParamsPresent(boolean allParamsPresent)
    {
        this.allParamsPresent = allParamsPresent;
    }

    public String getExceptionSummary()
    {
        return exceptionSummary;
    }

    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionSummary = exceptionMessage;
    }
}
