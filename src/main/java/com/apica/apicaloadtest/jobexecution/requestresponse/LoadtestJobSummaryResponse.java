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
package com.apica.apicaloadtest.jobexecution.requestresponse;

import com.apica.apicaloadtest.jobexecution.PerformanceSummary;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 *
 * @author andras.nemes
 */
public class LoadtestJobSummaryResponse
{
    private final String NL = "\r\n";

    @SerializedName("Link to testresults")
    private String linkToTestResults;
    @SerializedName("Performance summary")
    private PerformanceSummary performanceSummary;
    @SerializedName("exception")
    private String exception;
    @SerializedName("Self service job id")
    private int jobId;

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public String getLinkToTestResults()
    {
        return this.linkToTestResults;
    }

    public PerformanceSummary getPerformanceSummary()
    {
        return this.performanceSummary;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("--- Load test job summary message ---%n", new Object[0]));
        sb.append("Job id: ").append(getJobId()).append(NL);
        sb.append("Message date UTC: ").append(new Date()).append(NL);
        //sb.append("Link to results: ").append(getLinkToTestResults()).append(NL);
        PerformanceSummary summary = getPerformanceSummary();
        sb.append("Total passed loops: ").append(summary.getTotalPassedLoops()).append(NL);
        sb.append("Total failed loops: ").append(summary.getTotalFailedLoops()).append(NL);
        sb.append("Average network throughput: ").append(summary.getAverageNetworkThroughput())
                .append(" ").append(summary.getNetworkThroughputUnit()).append(NL);
        sb.append("Average session time per loop (s): ")
                .append(summary.getAverageSessionTimePerLoop()).append(NL);
        sb.append("Average response time per loop (s): ")
                .append(summary.getAverageResponseTimePerLoop()).append(NL);
        sb.append("Web transaction rate (Hits/s): ")
                .append(summary.getWebTransactionRate()).append(NL);
        sb.append("Average response time per page (s): ")
                .append(summary.getAverageResponseTimePerPage()).append(NL);
        sb.append("Total http(s) calls: ")
                .append(summary.getTotalHttpCalls()).append(NL);
        sb.append("Avg network connect time (ms): ")
                .append(summary.getAverageNetworkConnectTime()).append(NL);
        sb.append("Total transmitted bytes: ")
                .append(summary.getTotalTransmittedBytes()).append(NL);
        sb.append(String.format("--- END ---%n", new Object[0]));
        return sb.toString();
    }
}
