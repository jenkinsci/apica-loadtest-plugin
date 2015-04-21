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

import com.apica.apicaloadtest.jobexecution.PerformanceSummary;

/**
 *
 * @author andras.nemes
 */
public class Threshold
{
    private final LoadtestThresholdMetric metric;
    private final ThresholdDirection direction;
    private final double numericValue;

    public Threshold(LoadtestThresholdMetric metric, ThresholdDirection direction, double numericValue)
    {
        this.metric = metric;
        this.direction = direction;
        this.numericValue = numericValue;
    }

    public LoadtestThresholdMetric getMetric()
    {
        return metric;
    }

    public ThresholdDirection getDirection()
    {
        return direction;
    }

    public double getNumericValue()
    {
        return numericValue;
    }
    
    public ThresholdEvaluationResult evaluate(PerformanceSummary performanceSummary)
    {
        ThresholdEvaluationResult res = new ThresholdEvaluationResult();
        double actualValue = metric.extractActualValueFrom(performanceSummary);
        boolean broken = direction.thresholdBroken(numericValue, actualValue);
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("If ")
                .append(metric.getDescription().toLowerCase())
                .append(" is ").append(direction.getDescription())
                .append(" ").append(numericValue).append(metric.getUnitOfMeasurementSymbol())
                .append(" then test fails. Outcome: ").append(actualValue).append(metric.getUnitOfMeasurementSymbol())
                .append(" Threshold broken: ").append(broken);
        res.setThresholdBroken(broken);
        res.setReport(reportBuilder.toString());
        return res;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("If ").append(metric.getDescription().toLowerCase()).append(" ").append(direction.getDescription())
                .append(" ").append(Double.toString(numericValue))
                .append(metric.getUnitOfMeasurementSymbol())
                .append(" then mark test as failed");

        return sb.toString();
    }
}
