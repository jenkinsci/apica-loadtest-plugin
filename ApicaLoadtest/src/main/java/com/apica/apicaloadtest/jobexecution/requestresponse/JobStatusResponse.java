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

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 *
 * @author andras.nemes
 */
public class JobStatusResponse
{
    @SerializedName("id")
    private int jobId;
    @SerializedName("message")
    private String statusMessage;
    @SerializedName("completed")
    private boolean completed;
    @SerializedName("error")
    private boolean jobFaulted;
    @SerializedName("exception")
    private String exception;

    public int getJobId()
    {
        return jobId;
    }

    public void setJobId(int jobId)
    {
        this.jobId = jobId;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public boolean isJobFaulted()
    {
        return jobFaulted;
    }

    public void setJobFaulted(boolean jobFaulted)
    {
        this.jobFaulted = jobFaulted;
    }

    public String getException()
    {
        return exception;
    }

    public void setException(String exception)
    {
        this.exception = exception;
    }

    public boolean isJobCompleted()
    {
        return jobFaulted || completed;
    }

    @Override
    public String toString()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("--- Load Test message ---%n", new Object[0]));
            sb.append("Job id: ").append(getJobId()).append("\r\n");
            sb.append("Message date UTC: ").append(new Date()).append("\r\n");
            sb.append("Job message: ").append(getStatusMessage()).append("\r\n");;
            sb.append(String.format("--- END ---%n", new Object[0]));
            return sb.toString();
        } catch (Exception ex)
        {
            return "Could not read job status response properties.";
        }
    }
}
