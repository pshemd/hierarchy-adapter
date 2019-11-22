package com.zyfra.mdmobjectsservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executors;

@Configuration
public class HttpConfig {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .executor(Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() * 4))
                .build();
    }

    @Bean("trustfulHttpClient")
    public HttpClient trustfulHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        var sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustfulManager()}, null);
        return HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
    }

    @Bean
    public TrustManager trustfulManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}
