package com.manhnguyen.codebase.data.api

import com.manhnguyen.codebase.data.model.SongInfo
import retrofit2.http.GET

interface SongApi {

    @GET("/songs")
    suspend fun getSongs(): List<SongInfo>
}