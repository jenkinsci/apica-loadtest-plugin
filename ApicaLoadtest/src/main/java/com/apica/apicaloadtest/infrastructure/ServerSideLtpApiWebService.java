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

import com.apica.apicaloadtest.environment.LoadtestEnvironment;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author andras.nemes
 */
public class ServerSideLtpApiWebService
{

    private final String baseLtpApiUri;
    private final String uriVersion;
    private final String tokenQueryStub = "token";
    private final String separator = "/";
    private final String scheme = "http";
    private final int port = 80;

    public ServerSideLtpApiWebService(LoadtestEnvironment loadtestEnvironment)
    {
        this.baseLtpApiUri = loadtestEnvironment.getLtpWebServiceBaseUrl();
        this.uriVersion = loadtestEnvironment.getLtpWebServiceVersion();
    }

    public PresetResponse checkPreset(String authToken, String presetName)
    {
        String presetUriExtension = "selfservicepresets";
        PresetResponse presetResponse = new PresetResponse();
        presetResponse.setException("");
        try
        {
            String tokenExtension = tokenQueryStub.concat("=").concat(authToken);
            URI presetUri = new URI(scheme, null, baseLtpApiUri, port, separator.concat(uriVersion).concat(separator)
                    .concat(presetUriExtension).concat(separator).concat(presetName), tokenExtension, null);
            WebRequestOutcome presetRequestOutcome = makeWebRequest(presetUri);
            if (presetRequestOutcome.isWebRequestSuccessful())
            {
                if (presetRequestOutcome.getHttpResponseCode() < 300)
                {
                    Gson gson = new Gson();
                    presetResponse = gson.fromJson(presetRequestOutcome.getRawResponseContent(), PresetResponse.class);
                } else
                {
                    presetResponse.setException(presetRequestOutcome.getExceptionMessage());
                }
            } else
            {
                presetResponse.setException(presetRequestOutcome.getExceptionMessage());
            }
        } catch (URISyntaxException ex)
        {
            presetResponse.setException(ex.getMessage());
        }

        return presetResponse;
    }
    
    public RunnableFileResponse checkRunnableFile(String authToken, String fileName)
    {
        String fileUriExtension = "selfservicefiles";
        RunnableFileResponse runnableFileResponse = new RunnableFileResponse();
        runnableFileResponse.setException("");
        try
        {
            String tokenExtension = tokenQueryStub.concat("=").concat(authToken);
            URI fileUri = new URI(scheme, null, baseLtpApiUri, port, separator.concat(uriVersion).concat(separator)
                    .concat(fileUriExtension).concat(separator).concat(fileName), tokenExtension, null);
            WebRequestOutcome fileRequestOutcome = makeWebRequest(fileUri);
            if (fileRequestOutcome.isWebRequestSuccessful())
            {
                if (fileRequestOutcome.getHttpResponseCode() < 300)
                {
                    Gson gson = new Gson();
                    runnableFileResponse = gson.fromJson(fileRequestOutcome.getRawResponseContent(), RunnableFileResponse.class);
                } else
                {
                    runnableFileResponse.setException(fileRequestOutcome.getExceptionMessage());
                }
            } else
            {
                runnableFileResponse.setException(fileRequestOutcome.getExceptionMessage());
            }
        } catch (URISyntaxException ex)
        {
            runnableFileResponse.setException(ex.getMessage());
        }
        return runnableFileResponse;
    }
    
    private WebRequestOutcome makeWebRequest(URI uri)
    {
        WebRequestOutcome outcome = new WebRequestOutcome();
        outcome.setExceptionMessage("");
        try
        {
            URL presetUrl = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) presetUrl.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(60000);
            int responseCode = con.getResponseCode();
            outcome.setHttpResponseCode(responseCode);
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                outcome.setRawResponseContent(response.toString());
                outcome.setWebRequestSuccessful(true);
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            } else
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }

                outcome.setExceptionMessage(response.toString());
                try
                {
                    in.close();
                } catch (IOException ex)
                {
                }
            }
        } catch (IOException ex)
        {
            outcome.setExceptionMessage(ex.getMessage());
            outcome.setRawResponseContent("");
        }
        return outcome;
    }
}
