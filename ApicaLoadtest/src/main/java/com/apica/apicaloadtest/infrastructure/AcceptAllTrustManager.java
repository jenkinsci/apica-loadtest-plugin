/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apica.apicaloadtest.infrastructure;

import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 *
 * @author andras.nemes
 */
public class AcceptAllTrustManager implements SslTrustManager
{

    @Override
    public TrustManager[] certificatesToTrust()
    {
        return new TrustManager[]
        {
            new X509TrustManager()
            {
                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null; // Not relevant.
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType)
                {
                    // Do nothing. Just allow them all.
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType)
                {
                    // Do nothing. Just allow them all.
                }
            }
        };
    }
}