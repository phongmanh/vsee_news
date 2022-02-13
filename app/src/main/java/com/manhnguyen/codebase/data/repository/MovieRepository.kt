package com.manhnguyen.codebase.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.MovieDetail
import com.manhnguyen.codebase.data.model.MovieInfo
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
            api.movieApi.getDetails(movieId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            val movieInfo: MovieInfo = Gson().fromJson(responseBody.string(), object : TypeToken<MovieInfo>(){}.type)
                            val movie: MovieDetail.Movie = Gson().fromJson(responseBody.string(), object : TypeToken<MovieDetail.Movie>(){}.type)
                            cont.resume(movie)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    cont.resumeWithException(t)
                }
            })
        }catch (e: Exception){
            cont.resumeWithException(e)
        }
    }

}