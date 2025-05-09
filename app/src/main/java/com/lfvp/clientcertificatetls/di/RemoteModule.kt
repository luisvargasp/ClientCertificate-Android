package com.lfvp.clientcertificatetls.di

import com.lfvp.clientcertificatetls.TEST_URL
import com.lfvp.clientcertificatetls.remote.AssetsTLSInterceptor
import com.lfvp.clientcertificatetls.remote.BadSSLAPI
import com.lfvp.clientcertificatetls.certificate.CertificateProvider
import com.lfvp.clientcertificatetls.remote.SystemTLSInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {
    @Provides
    @Singleton
    fun provideBaseRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(TEST_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    @Singleton
    @Named("api")

    fun provideBadSSLApiUnAuthenticated(builder: Retrofit.Builder): BadSSLAPI {
        return builder.build().create(BadSSLAPI::class.java)
    }

    @Provides
    @Singleton
    @Named("systemApi")

    fun provideBadSSLApiSystemAuthenticated(
        builder: Retrofit.Builder,
        @Named("systemClient") client: OkHttpClient
    ): BadSSLAPI {
        return builder.client(client)
            .build().create(BadSSLAPI::class.java)
    }

    @Provides
    @Singleton
    @Named("assetsApi")
    fun provideBadSSLApiAssetsAuthenticated(
        builder: Retrofit.Builder,
        @Named("assetsClient") client: OkHttpClient
    ): BadSSLAPI {
        return builder.client(client)
            .build().create(BadSSLAPI::class.java)
    }

    @Provides
    @Singleton
    @Named("systemClient")
    fun provideSystemAuthenticatedClient(systemTLSInterceptor: SystemTLSInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(systemTLSInterceptor).build()

    }

    @Provides
    @Singleton
    @Named("assetsClient")
    fun provideAssetsAuthenticatedClient(assetsTLSInterceptor: AssetsTLSInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(assetsTLSInterceptor).build()

    }


    @Provides
    @Singleton
    fun provideTrustManager(): X509TrustManager {
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        return trustManagerFactory.trustManagers
            .filterIsInstance<X509TrustManager>()
            .first()
    }

    @Provides
    @Singleton
    fun provideSystemTLSInterceptor(
        @Named("systemCertificateProvider")
        certificateProvider: CertificateProvider,
        trustManager: X509TrustManager
    ) = SystemTLSInterceptor(
        certificateProvider,
        trustManager
    )

    @Provides
    @Singleton
    fun provideAssetsInterceptor(
        @Named("assetsCertificateProvider") certificateProvider: CertificateProvider,
        trustManager: X509TrustManager
    ) =
        AssetsTLSInterceptor(certificateProvider, trustManager)


}