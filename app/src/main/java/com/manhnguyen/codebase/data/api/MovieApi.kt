package com.manhnguyen.codebase.data.api

import com.manhnguyen.codebase.data.model.MovieDetail
import com.manhnguyen.codebase.data.model.MovieInfoResponse
import com.manhnguyen.codebase.data.model.NowPlaying
import com.manhnguyen.codebase.data.model.TopRate
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("top_rated")
    suspend fun getTopRatePaging(@Query("page") page: Int): MovieInfoResponse

    @GET("now_playing")
    suspend fun getNowPlayingPaging(@Query("page") page: Int): NowPlaying

    @GET("top_rated")
    suspend fun getTopRate(@Query("page") page: Int): TopRate

    @GET("now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int): NowPlaying

    @GET("{movie_id}?")
    suspend fun getDetail(@Path("movie_id") movie_id: Int): MovieDetail.Movie

    @GET("{movie_id}?")
    fun getDetails(@Path("movie_id") movie_id: Int): Call<ResponseBody>

}