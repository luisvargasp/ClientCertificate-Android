package com.lfvp.clientcertificatetls.di

import android.content.Context
import android.content.SharedPreferences
import com.lfvp.clientcertificatetls.preferences.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferencesManager(sharedPreferences: SharedPreferences) =
        PreferencesManager(sharedPreferences)
}