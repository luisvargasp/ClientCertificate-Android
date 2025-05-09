package com.lfvp.clientcertificatetls.certificate

import android.content.Context
import com.lfvp.clientcertificatetls.P12_CERTIFICATE_FILE_NAME
import com.lfvp.clientcertificatetls.P12_CERTIFICATE_PASSWORD
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyStore
import javax.inject.Inject
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
//works for .p12 and pfx
class P12AssetsCertificateProvider @Inject constructor(private val context: Context) :
    CertificateProvider {
    override fun getSSLContext(): SSLContext {
        val certificateFile = context.assets.open(P12_CERTIFICATE_FILE_NAME)

        //val keyStore = KeyStore.getInstance("PKCS12")
        val keyStore = KeyStore.getInstance("PKCS12", BouncyCastleProvider())
        keyStore.load(certificateFile, P12_CERTIFICATE_PASSWORD.toCharArray())

        val keyManagerFactory =
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, P12_CERTIFICATE_PASSWORD.toCharArray())
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagerFactory.keyManagers, null, null)
        return sslContext

    }
}