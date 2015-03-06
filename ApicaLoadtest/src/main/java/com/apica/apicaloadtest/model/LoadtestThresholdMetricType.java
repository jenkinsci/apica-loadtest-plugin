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

/**
 *
 * @author andras.nemes
 */
public enum LoadtestThresholdMetricType
{
    AVERAGE_RESPONSE_TIME_PER_PAGE("resp_time_per_page", "Average response time per page"), FAILED_LOOPS_RATE("failed_loops", "Failed loops rate");

    private final String value;
    private final String description;

    private LoadtestThresholdMetricType(String value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }

    public static LoadtestThresholdMetricType get(String val)
    {
        for (LoadtestThresholdMetricType parameterType : LoadtestThresholdMetricType.values())
        {
            if (val.equals(parameterType.getValue()))
            {
                return parameterType;
            }
        }
        return FAILED_LOOPS_RATE;
    }
}
