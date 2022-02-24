package com.manhnguyen.codebase.data.api

import com.manhnguyen.codebase.data.model.Configuration
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ConfigApi {

    @GET("configuration")
    suspend fun fetchConfig(): Response<Configuration>
}