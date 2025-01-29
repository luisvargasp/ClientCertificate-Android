package com.lfvp.clientcertificatetls.certificate

import android.content.Context
import android.security.KeyChain
import com.lfvp.clientcertificatetls.CERTIFICATE_ALIAS
import com.lfvp.clientcertificatetls.preferences.PreferencesManager
import java.security.KeyStore
import javax.inject.Inject
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

class SystemCertificateProvider @Inject constructor(
    private val preferencesManager: PreferencesManager, val context: Context
) : CertificateProvider {
    override fun getSSLContext(): SSLContext {
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, null, null)
        }
        preferencesManager.get(CERTIFICATE_ALIAS)?.let {
            val keyStore = KeyStore.getInstance("PKCS12")
            keyStore.load(null, null)
            keyStore.setKeyEntry(
                "X",
                KeyChain.getPrivateKey(context, it),
                null,
                KeyChain.getCertificateChain(context, it)
            )

            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore, null)
            }
        }?.let {
            sslContext.init(it.keyManagers, null, null)
        }
        return sslContext

    }
}