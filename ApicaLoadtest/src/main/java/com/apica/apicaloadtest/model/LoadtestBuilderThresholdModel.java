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

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import java.util.Arrays;
import java.util.List;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author andras.nemes
 */
public class LoadtestBuilderThresholdModel extends AbstractDescribableImpl<LoadtestBuilderThresholdModel>
{
    private final String metricName;
    
    public static final List<String> metricNames = Arrays.asList(LoadtestThresholdMetricType.AVERAGE_RESPONSE_TIME_PER_PAGE.getValue(),
        LoadtestThresholdMetricType.FAILED_LOOPS_RATE.getValue());
    
    @DataBoundConstructor
    public LoadtestBuilderThresholdModel(String metricName)
    {
        this.metricName = metricName;
    }

    public String getMetricName()
    {
        return metricName;
    }
    
    @Extension
    public static class DescriptorImlp extends Descriptor<LoadtestBuilderThresholdModel>
    {
        public String getDisplayName() 
        {
            return "Threshold metric";
        }
        
        public List<String> getThresholdMetricTypes()
        {
            return LoadtestBuilderThresholdModel.metricNames;
        }
    }
}
