package com.lfvp.clientcertificatetls.di

import android.content.Context
import com.lfvp.clientcertificatetls.certificate.CertificateProvider
import com.lfvp.clientcertificatetls.certificate.P12AssetsCertificateProvider
import com.lfvp.clientcertificatetls.preferences.PreferencesManager
import com.lfvp.clientcertificatetls.certificate.SystemCertificateProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class CertificateModule {


    @Provides
    @Singleton
    @Named("assetsCertificateProvider")
    fun provideP12AssetsCertificateProvider(@ApplicationContext context: Context):
            CertificateProvider = P12AssetsCertificateProvider(context)

    @Provides
    @Singleton
    @Named("systemCertificateProvider")
    fun provideSystemCertificateProvider(
        preferencesManager: PreferencesManager,
        @ApplicationContext context: Context
    ):
            CertificateProvider = SystemCertificateProvider(preferencesManager, context)


}