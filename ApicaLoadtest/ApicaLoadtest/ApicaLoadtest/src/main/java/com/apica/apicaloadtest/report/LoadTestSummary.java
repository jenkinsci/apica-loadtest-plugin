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

import com.apica.apicaloadtest.jobexecution.PerformanceSummary;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import java.util.Date;

/**
 *
 * @author andras.nemes
 */
public class LoadTestSummary implements Action
{
    private final AbstractBuild<?, ?> build;
    private final PerformanceSummary performanceSummary;
    private final Date buildStartDate;
    private final String presetName;

    public LoadTestSummary(AbstractBuild<?, ?> build, 
            PerformanceSummary performanceSummary, String presetName)
    {
        this.build = build;
        this.buildStartDate = new Date(build.getStartTimeInMillis());
        this.presetName = presetName;
        if (performanceSummary == null)
        {
            this.performanceSummary = new PerformanceSummary();
        } else
        {
            this.performanceSummary = performanceSummary;
        }
    }

    

    @Override
    public String getIconFileName()
    {
        return "clipboard.gif";
    }

    @Override
    public String getDisplayName()
    {
        return "Apica load test summary";
    }

    @Override
    public String getUrlName()
    {
        return "loadtest-summary";
    }

    public AbstractBuild<?, ?> getBuild()
    {
        return this.build;
    }

    public PerformanceSummary getPerformanceSummary()
    {
        return performanceSummary;
    }

    public String getPresetName()
    {
        return presetName;
    }
    
    public Date getBuildStartDate()
    {
        return buildStartDate;
    }    
}
