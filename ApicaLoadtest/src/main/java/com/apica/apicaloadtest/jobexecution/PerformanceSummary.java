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
package com.apica.apicaloadtest.jobexecution;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author andras.nemes
 */
public class PerformanceSummary
{
    @SerializedName("Total passed loops")
    private int totalPassedLoops;
    @SerializedName("Total failed loops")
    private int totalFailedLoops;
    @SerializedName("Average network throughput")
    private double averageNetworkThroughput;
    @SerializedName("Avg. network throughput unit of measurement")
    private String networkThroughputUnit;
    @SerializedName("Average session time per loop (s)")
    private double averageSessionTimePerLoop;
    @SerializedName("Average response time per loop (s)")
    private double averageResponseTimePerLoop;
    @SerializedName("Web transaction rate (Hits/s)")
    private double webTransactionRate;
    @SerializedName("Average response time per page (s)")
    private double averageResponseTimePerPage;
    @SerializedName("Total http(s) calls")
    private int totalHttpCalls;
    @SerializedName("Avg network connect time (ms)")
    private int averageNetworkConnectTime;
    @SerializedName("Total transmitted bytes")
    private long totalTransmittedBytes;
    
    public PerformanceSummary()
    {
        
    }

    
    
    public int getTotalPassedLoops()
    {
        return totalPassedLoops;
    }

    public void setTotalPassedLoops(int totalPassedLoops)
    {
        this.totalPassedLoops = totalPassedLoops;
    }

    public int getTotalFailedLoops()
    {
        return totalFailedLoops;
    }

    public void setTotalFailedLoops(int totalFailedLoops)
    {
        this.totalFailedLoops = totalFailedLoops;
    }

    public double getAverageNetworkThroughput()
    {
        return averageNetworkThroughput;
    }

    public void setAverageNetworkThroughput(double averageNetworkThroughput)
    {
        this.averageNetworkThroughput = averageNetworkThroughput;
    }

    public String getNetworkThroughputUnit()
    {
        return networkThroughputUnit;
    }

    public void setNetworkThroughputUnit(String networkThroughputUnit)
    {
        this.networkThroughputUnit = networkThroughputUnit;
    }

    public double getAverageSessionTimePerLoop()
    {
        return averageSessionTimePerLoop;
    }

    public void setAverageSessionTimePerLoop(double averageSessionTimePerLoop)
    {
        this.averageSessionTimePerLoop = averageSessionTimePerLoop;
    }

    public double getAverageResponseTimePerLoop()
    {
        return averageResponseTimePerLoop;
    }

    public void setAverageResponseTimePerLoop(double averageResponseTimePerLoop)
    {
        this.averageResponseTimePerLoop = averageResponseTimePerLoop;
    }

    public double getWebTransactionRate()
    {
        return webTransactionRate;
    }

    public void setWebTransactionRate(double webTransactionRate)
    {
        this.webTransactionRate = webTransactionRate;
    }

    public double getAverageResponseTimePerPage()
    {
        return averageResponseTimePerPage;
    }

    public void setAverageResponseTimePerPage(double averageResponseTimePerPage)
    {
        this.averageResponseTimePerPage = averageResponseTimePerPage;
    }

    public int getTotalHttpCalls()
    {
        return totalHttpCalls;
    }

    public void setTotalHttpCalls(int totalHttpCalls)
    {
        this.totalHttpCalls = totalHttpCalls;
    }

    public int getAverageNetworkConnectTime()
    {
        return averageNetworkConnectTime;
    }

    public void setAverageNetworkConnectTime(int averageNetworkConnectTime)
    {
        this.averageNetworkConnectTime = averageNetworkConnectTime;
    }

    public long getTotalTransmittedBytes()
    {
        return totalTransmittedBytes;
    }

    public void setTotalTransmittedBytes(long totalTransmittedBytes)
    {
        this.totalTransmittedBytes = totalTransmittedBytes;
    }
}
