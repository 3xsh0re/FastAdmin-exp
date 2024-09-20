package org.example.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class OkHttpUtils {
    public static Response sendRequest(String TargetUrl) throws Exception {
        // 创建一个信任所有证书的 TrustManager
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];  // 返回空数组而不是null
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // 初始化一个 SSLContext，使用我们自定义的 TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // 创建 OkHttpClient 并禁用 SSL 验证和主机名验证
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)  // 禁用主机名验证
                .build();

        // 创建请求
        Request request = new Request.Builder()
                .url(TargetUrl)
                .build();

        // 发送请求并获取响应
        return client.newCall(request).execute();
    }
}
