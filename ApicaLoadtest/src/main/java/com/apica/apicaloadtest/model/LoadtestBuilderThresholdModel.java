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
import hudson.util.FormValidation;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 *
 * @author andras.nemes
 */
public class LoadtestBuilderThresholdModel extends AbstractDescribableImpl<LoadtestBuilderThresholdModel>
{

    private final String metricType;
    private final String evaluationDirection;
    private final LoadtestThresholdMetric loadtestThresholdMetric;
    private final ThresholdDirection thresholdDirection;
    private final int numericValue;

    public static final List<LoadtestThresholdMetric> metricTypes
            = LoadtestThresholdMetricFactory.getAllMetrics();

    public static final List<ThresholdDirection> evaluationDirections
            = ThresholdDirectionFactory.getThresholdDirections();

    @DataBoundConstructor
    public LoadtestBuilderThresholdModel(String metricType, String evaluationDirection, int numericValue)
    {
        this.metricType = metricType;
        this.evaluationDirection = evaluationDirection;
        loadtestThresholdMetric = LoadtestThresholdMetricFactory.getMetric(metricType);
        thresholdDirection = ThresholdDirectionFactory.getThresholdDirection(evaluationDirection);
        this.numericValue = numericValue;
    }

    public String getMetricType()
    {
        return metricType;
    }

    public String getEvaluationDirection()
    {
        return evaluationDirection;
    }

    public LoadtestThresholdMetric getLoadtestThresholdMetric()
    {
        return loadtestThresholdMetric;
    }

    public ThresholdDirection getThresholdDirection()
    {
        return thresholdDirection;
    }

    public int getNumericValue()
    {
        return numericValue;
    }
    
    @Extension
    public static class DescriptorImlp extends Descriptor<LoadtestBuilderThresholdModel>
    {

        public String getDisplayName()
        {
            return "Threshold metric";
        }

        public List<LoadtestThresholdMetric> getMetricTypes()
        {
            return LoadtestBuilderThresholdModel.metricTypes;
        }

        public List<ThresholdDirection> getEvaluationDirections()
        {
            return LoadtestBuilderThresholdModel.evaluationDirections;
        }

        public FormValidation doCheckNumericValue(@QueryParameter String value)
                throws IOException, ServletException
        {
            if (value.length() == 0)
            {
                return FormValidation.error("Please insert a valid positive integer.");
            }
            if (!isNumeric(value))
            {
                return FormValidation.error("Please insert a valid positive integer.");
            }
            int converted = Integer.parseInt(value);
            if (converted < 0)
            {
                return FormValidation.error("Please insert a valid positive integer.");
            }
            return FormValidation.ok();
        }

        private boolean isNumeric(String str)
        {
            try
            {
                int d = Integer.parseInt(str);
            } catch (NumberFormatException nfe)
            {
                return false;
            }
            return true;
        }
    }
}
