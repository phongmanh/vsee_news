package com.manhnguyen.codebase.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.MovieDetail
import com.manhnguyen.codebase.data.model.MovieInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.contracts.contract
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MovieRepository constructor(private val api: Api) {

    suspend fun getTopRateMovies(page: Int) = api.movieApi.getTopRate(page)

    suspend fun getNowPlayingMovies(page: Int) = api.movieApi.getNowPlaying(page)

    suspend fun getMovieDetail(movieId: Int) = api.movieApi.getDetail(movieId)

    suspend fun getDetails(movieId: Int) = suspendCoroutine<MovieDetail.Movie> { cont ->
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val response = api.movieApi.getDetails(movieId)
                if (response.isSuccessful) {
                    cont.resume(response.body()!!)
                } else {
                    cont.resumeWithException(Throwable("Empty"))
                }
            }
        } catch (e: Throwable) {
            cont.resumeWithException(e)
        }
    }

}