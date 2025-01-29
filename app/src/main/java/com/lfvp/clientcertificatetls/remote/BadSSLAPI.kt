package com.lfvp.clientcertificatetls.remote

import retrofit2.Response
import retrofit2.http.GET

interface BadSSLAPI {
    @GET("?")
    suspend fun test(): Response<Unit>

}