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

import com.apica.apicaloadtest.jobexecution.requestresponse.TransmitJobPresetArgs;
import com.apica.apicaloadtest.jobexecution.requestresponse.TransmitJobRequestArgs;
import com.apica.apicaloadtest.jobexecution.requestresponse.PresetResponse;
import com.apica.apicaloadtest.jobexecution.requestresponse.RunnableFileResponse;
import com.apica.apicaloadtest.jobexecution.requestresponse.JobStatusResponse;
import com.apica.apicaloadtest.jobexecution.requestresponse.LoadtestJobSummaryRequest;
import com.apica.apicaloadtest.jobexecution.requestresponse.LoadtestJobSummaryResponse;
import com.apica.apicaloadtest.jobexecution.requestresponse.StartJobByPresetResponse;
import com.apica.apicaloadtest.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 *
 * @author andras.nemes
 */
public class ServerSideLtpApiWebService
{

    private final String baseLtpApiUri;
    private final String tokenQueryStub = "token";
    private final String separator = "/";
    private String scheme = "http";
    private int port = 80;

    public ServerSideLtpApiWebService(String apiBaseUrl) throws MalformedURLException
    {
        this.baseLtpApiUri = apiBaseUrl;
        if (this.baseLtpApiUri.startsWith("https://"))
        {
            scheme = "https";
        }
        URL url = new URL(apiBaseUrl);
        port = url.getPort();
        if (port == -1)
        {
            if (scheme.equalsIgnoreCase("http"))
            {
                port = 80;
            } else if (scheme.equalsIgnoreCase("https"))
            {
                port = 449;
            }
        }
    }

    public PresetResponse checkPreset(String authToken, String presetName)
    {
        String presetUriExtension = "selfservicepresets";
        PresetResponse presetResponse = new PresetResponse();
        presetResponse.setException("");

        try
        {
            String tokenExtension = tokenQueryStub.concat("=").concat(authToken);
            String normalisedUrl = baseLtpApiUri.endsWith(separator) ? baseLtpApiUri : baseLtpApiUri.concat(separator);
            String presetUrl = normalisedUrl.concat(presetUriExtension).concat(separator).concat(presetName.replace(" ", "%20"))
                    .concat("?")
                    .concat(tokenExtension);
            URI presetUri = new URI(presetUrl);

            WebRequestOutcome presetRequestOutcome = makeBodyLessWebRequest(presetUri);
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
            presetResponse.setException("URL syntax exception: ".concat(ex.getMessage()));
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
            String normalisedUrl = baseLtpApiUri.endsWith(separator) ? baseLtpApiUri : baseLtpApiUri.concat(separator);
            String fileUrl = normalisedUrl.concat(fileUriExtension).concat(separator).concat(fileName)
                    .concat("?")
                    .concat(tokenExtension);
            URI fileUri = new URI(fileUrl);
            WebRequestOutcome fileRequestOutcome = makeBodyLessWebRequest(fileUri);
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
            runnableFileResponse.setException("URL syntax exception: ".concat(ex.getMessage()));
        }
        return runnableFileResponse;
    }

    public StartJobByPresetResponse transmitJob(TransmitJobRequestArgs transmitJobArgs) throws URISyntaxException, IOException
    {
        StartJobByPresetResponse resp = new StartJobByPresetResponse();
        String startByPresetUriExtension = "selfservicejobs/preset";
        String authToken = transmitJobArgs.getAuthToken();
        String tokenExtension = tokenQueryStub.concat("=").concat(authToken);
        String normalisedUrl = baseLtpApiUri.endsWith(separator) ? baseLtpApiUri : baseLtpApiUri.concat(separator);
        String startByPresetUrl = normalisedUrl.concat(startByPresetUriExtension)
                .concat("?")
                .concat(tokenExtension);
        URI startByPresetUri = new URI(startByPresetUrl);
        resp.setException("");
        resp.setJobId(-1);
        HttpUrlConnectionArguments connectionArgs = new HttpUrlConnectionArguments();
        connectionArgs.setContentType("application/json");
        connectionArgs.setWebMethod("POST");
        connectionArgs.setAuthToken(transmitJobArgs.getAuthToken());
        Gson gson = new com.google.gson.Gson();
        TransmitJobPresetArgs presetArgs = new TransmitJobPresetArgs(transmitJobArgs.getPresetName(), transmitJobArgs.getFileName());
        String jsonifiedPresetArgs = gson.toJson(presetArgs);
        connectionArgs.setRequestBody(jsonifiedPresetArgs);
        connectionArgs.setUri(startByPresetUri);
        try
        {
            HttpURLConnection con = null;
            if (scheme.equalsIgnoreCase("https"))
            {
                con = buildHttpsUrlRequest(connectionArgs);
            } else
            {
                con = buildHttpUrlRequest(connectionArgs);
            }
            int responseCode = con.getResponseCode();
            if (responseCode < 300)
            {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8.name())))
                {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }

                    resp = gson.fromJson(response.toString(), StartJobByPresetResponse.class);
                }
            } else
            {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8.name())))
                {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                    resp.setException(response.toString());
                }
            }
        } catch (JsonSyntaxException | IOException ex)
        {
            resp.setException(ex.getMessage());
        }
        return resp;
    }

    public JobStatusResponse checkJobStatus(JobStatusRequest jobStatusRequest) throws URISyntaxException
    {
        JobStatusResponse resp = new JobStatusResponse();
        resp.setException("");
        String jobStatusExtension = "selfservicejobs";
        int jobId = jobStatusRequest.getJobId();
        String authToken = jobStatusRequest.getAuthToken();
        String tokenExtension = tokenQueryStub.concat("=").concat(authToken);
        String normalisedUrl = baseLtpApiUri.endsWith(separator) ? baseLtpApiUri : baseLtpApiUri.concat(separator);
        String startByPresetUrl = normalisedUrl.concat(jobStatusExtension)
                .concat(separator).concat(Integer.toString(jobId))
                .concat("?")
                .concat(tokenExtension);
        URI jobStatusUri = new URI(startByPresetUrl);
        HttpUrlConnectionArguments connectionArgs = new HttpUrlConnectionArguments();
        connectionArgs.setWebMethod("GET");
        connectionArgs.setAuthToken(jobStatusRequest.getAuthToken());
        connectionArgs.setUri(jobStatusUri);
        Gson gson = new Gson();
        try
        {
            HttpURLConnection con = null;
            if (scheme.equalsIgnoreCase("https"))
            {
                con = buildHttpsUrlRequest(connectionArgs);
            } else
            {
                con = buildHttpUrlRequest(connectionArgs);
            }
            int responseCode = con.getResponseCode();
            if (responseCode < 300)
            {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8.name())))
                {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                    resp = gson.fromJson(response.toString(), JobStatusResponse.class);
                }
            } else
            {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8.name())))
                {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                    resp.setException(response.toString());
                }
            }
        } catch (JsonSyntaxException ex)
        {
            resp.setException("Json exception: ".concat(ex.getMessage()));
        } catch (IOException ex)
        {
            resp.setException(ex.getMessage());
        }
        return resp;
    }

    public LoadtestJobSummaryResponse getJobSummaryResponse(LoadtestJobSummaryRequest summaryRequest) throws URISyntaxException
    {
        LoadtestJobSummaryResponse resp = new LoadtestJobSummaryResponse();
        resp.setException("");
        String summaryEndpoint = "summary";
        String jobStatusExtension = "selfservicejobs";
        String authToken = summaryRequest.getAuthToken();
        String tokenExtension = tokenQueryStub.concat("=").concat(authToken);
        String normalisedUrl = baseLtpApiUri.endsWith(separator) ? baseLtpApiUri : baseLtpApiUri.concat(separator);
        int jobId = summaryRequest.getJobId();
        String startByPresetUrl = normalisedUrl.concat(jobStatusExtension)
                .concat(separator).concat(Integer.toString(jobId))
                .concat(separator)
                .concat(summaryEndpoint)
                .concat("?")
                .concat(tokenExtension);

        URI jobSummaryUri = new URI(startByPresetUrl);
        HttpUrlConnectionArguments connectionArgs = new HttpUrlConnectionArguments();
        connectionArgs.setWebMethod("GET");
        connectionArgs.setAuthToken(authToken);
        connectionArgs.setUri(jobSummaryUri);
        Gson gson = new Gson();
        try
        {
            HttpURLConnection con = null;
            if (scheme.equalsIgnoreCase("https"))
            {
                con = buildHttpsUrlRequest(connectionArgs);
            } else
            {
                con = buildHttpUrlRequest(connectionArgs);
            }
            int responseCode = con.getResponseCode();
            if (responseCode < 300)
            {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8.name())))
                {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                    resp = gson.fromJson(response.toString(), LoadtestJobSummaryResponse.class);
                }
            } else
            {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8.name())))
                {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                    resp.setException(response.toString());
                }
            }
        } catch (JsonSyntaxException | IOException ex)
        {
            resp.setException(ex.getMessage());
        }
        return resp;
    }

    private WebRequestOutcome makeBodyLessWebRequest(URI uri)
    {
        WebRequestOutcome outcome = new WebRequestOutcome();
        outcome.setExceptionMessage("");
        try
        {
            URL presetUrl = uri.toURL();
            String urlString = presetUrl.toString();
            HttpURLConnection con = null;
            if (urlString.startsWith("https"))
            {
                initSSL();
                con = (HttpsURLConnection) presetUrl.openConnection();
            } else
            {
                con = (HttpURLConnection) presetUrl.openConnection();
            }
            con.setRequestMethod("GET");
            con.setConnectTimeout(60000);
            int responseCode = con.getResponseCode();
            outcome.setHttpResponseCode(responseCode);
            if (responseCode < 300)
            {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8.name()));

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
                        new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8.name()));

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
            outcome.setExceptionMessage("Connectivity failure while contacting the URL: ".concat(ex.getMessage()));
            outcome.setRawResponseContent("");
        }
        return outcome;
    }

    private HttpsURLConnection buildHttpsUrlRequest(HttpUrlConnectionArguments connectionArguments) throws MalformedURLException, IOException
    {
        initSSL();
        HttpsURLConnection connection = null;
        URL url = connectionArguments.getUri().toURL();
        connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod(connectionArguments.getWebMethod());
        if (!Utils.isNullOrEmpty(connectionArguments.getContentType()))
        {
            connection.setRequestProperty("Content-Type", connectionArguments.getContentType());
        }
        if (!Utils.isNullOrEmpty(connectionArguments.getRequestBody()))
        {
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(connectionArguments.getRequestBody());
            wr.flush();
            wr.close();
        }
        return connection;
    }

    private HttpURLConnection buildHttpUrlRequest(HttpUrlConnectionArguments connectionArguments) throws MalformedURLException, IOException
    {
        HttpURLConnection connection = null;
        URL url = connectionArguments.getUri().toURL();
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(connectionArguments.getWebMethod());
        if (!Utils.isNullOrEmpty(connectionArguments.getContentType()))
        {
            connection.setRequestProperty("Content-Type", connectionArguments.getContentType());
        }
        if (!Utils.isNullOrEmpty(connectionArguments.getRequestBody()))
        {
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(connectionArguments.getRequestBody());
            wr.flush();
            wr.close();
        }
        return connection;
    }

    private void initSSL() throws IOException
    {

        try
        {
            TrustManager[] trustAllCertificates = new AcceptAllTrustManager().certificatesToTrust();
            HostnameVerifier trustAllHostnames = new AcceptAllHostnameVerifier().hostnamesToTrust();
            System.setProperty("jsse.enableSNIExtension", "false");
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
        } catch (NoSuchAlgorithmException | KeyManagementException ex)
        {
            throw new IOException("SSL initialisation exception: ".concat(ex.getMessage()));
        }
    }
}
