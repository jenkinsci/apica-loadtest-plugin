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
package com.apica.apicaloadtest.infrastructure;

/**
 *
 * @author andras.nemes
 */
public class WebRequestOutcome
{
    private int httpResponseCode;
    private String rawResponseContent;
    private String exceptionMessage;
    private boolean webRequestSuccessful;

    public boolean isWebRequestSuccessful()
    {
        return webRequestSuccessful;
    }

    public void setWebRequestSuccessful(boolean webRequestSuccessful)
    {
        this.webRequestSuccessful = webRequestSuccessful;
    }

    public String getExceptionMessage()
    {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }

    public int getHttpResponseCode()
    {
        return httpResponseCode;
    }

    public void setHttpResponseCode(int httpResponseCode)
    {
        this.httpResponseCode = httpResponseCode;
    }

    public String getRawResponseContent()
    {
        return rawResponseContent;
    }

    public void setRawResponseContent(String rawResponseContent)
    {
        this.rawResponseContent = rawResponseContent;
    }
}