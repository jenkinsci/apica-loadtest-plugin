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
package com.apica.apicaloadtest.report;

import com.apica.apicaloadtest.environment.LoadtestEnvironment;
import hudson.model.AbstractBuild;
import hudson.model.Action;

/**
 *
 * @author andras.nemes
 */
public class LoadTestTrend implements Action
{
    private final AbstractBuild<?, ?> build;
    private final int presetTestInstance;
    private final String loadtestPortalCiController = "ContinuousIntegrationTeamCity";
    private final String resultUrl;

    public LoadTestTrend(AbstractBuild<?, ?> build, int presetTestInstance, String authToken, LoadtestEnvironment le)
    {
        this.build = build;
        this.presetTestInstance = presetTestInstance;
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(le.getLtpWebPortalRoot())
                .append(loadtestPortalCiController)
                .append("?testInstanceId=").append(this.presetTestInstance)
                .append("&authToken=").append(authToken);
        resultUrl = urlBuilder.toString();
    }

    public String getResultUrl()
    {
        return resultUrl;
    }

    public AbstractBuild<?, ?> getBuild()
    {
        return build;
    }
    
    @Override
    public String getIconFileName()
    {
        return "graph.png";
    }

    @Override
    public String getDisplayName()
    {
        return "Apica loadtest trends";
    }

    @Override
    public String getUrlName()
    {
        return "loadtest-trends";
    }
    
}
