package com.lfvp.clientcertificatetls.certificate

import javax.net.ssl.SSLContext

interface CertificateProvider {
    fun getSSLContext(): SSLContext
}