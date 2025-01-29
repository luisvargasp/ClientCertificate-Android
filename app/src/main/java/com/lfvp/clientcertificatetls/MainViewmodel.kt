package com.lfvp.clientcertificatetls

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfvp.clientcertificatetls.preferences.PreferencesManager
import com.lfvp.clientcertificatetls.remote.BadSSLAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewmodel @Inject constructor(
    @Named("api") val badSSLAPI: BadSSLAPI,
    @Named("systemApi") val systemBadSSLAPI: BadSSLAPI,
    @Named("assetsApi") val assetsBadSSLAPI: BadSSLAPI,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    var loading by mutableStateOf(false)
        private set

    var showMessage by mutableStateOf<String?>(null)
        private set

    var responseCode by mutableStateOf<Int?>(null)
        private set


    fun resetOneTimeEvents() {
        showMessage = null
    }

    fun testUnauthenticated() {
        responseCode = null
        loading = true
        viewModelScope.launch {
            val result = badSSLAPI.test()

            responseCode = result.code()
            loading = false

        }

    }

    fun testWithAssetsCertificate() {
        responseCode = null
        loading = true


        viewModelScope.launch {
            val result = assetsBadSSLAPI.test()

            responseCode = result.code()
            loading = false


        }

    }

    fun testWithSystemCertificate(systemAlias: String?) {

        responseCode = null
        loading = true

        viewModelScope.launch(Dispatchers.IO) {

            if(systemAlias!=null){
                preferencesManager.save(CERTIFICATE_ALIAS, systemAlias)
            }else{
                preferencesManager.clear(CERTIFICATE_ALIAS)
            }
            val result = systemBadSSLAPI.test()

            responseCode = result.code()
            loading = false

        }
    }

    fun cleanResult() {
        responseCode = null
    }

}