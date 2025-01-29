package com.lfvp.clientcertificatetls.remote

import com.lfvp.clientcertificatetls.certificate.CertificateProvider
import javax.inject.Inject
import javax.net.ssl.X509TrustManager

class SystemTLSInterceptor @Inject constructor(
    certificateProvider:
    CertificateProvider, trustManager: X509TrustManager
) : BaseInterceptor(certificateProvider, trustManager)