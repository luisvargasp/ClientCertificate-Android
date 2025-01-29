package com.lfvp.clientcertificatetls.remote

import com.lfvp.clientcertificatetls.certificate.CertificateProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.net.ssl.X509TrustManager

open class BaseInterceptor(
    private val certificateProvider: CertificateProvider,
    private val trustManager: X509TrustManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val client = OkHttpClient.Builder()
            .sslSocketFactory(certificateProvider.getSSLContext().socketFactory, trustManager)
            .build()
        return client.newCall(request).execute()
    }
}