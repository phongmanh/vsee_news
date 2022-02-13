package com.manhnguyen.codebase.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.*
import com.manhnguyen.codebase.data.repository.MovieDataSource
import com.manhnguyen.codebase.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow


class MovieViewModel(val api: Api, private val movieRepository: MovieRepository) :
    ViewModel() {

    fun getNowPlaying(page: Int): LiveData<Result<NowPlaying>> = liveData {
        emit(Result.Loading())
        try {
            val value = movieRepository.getNowPlayingMovies(page)
            emit(Result.Success(value))
        } catch (e: Exception) {
            emit(Result.Error(ResponseError("", 0)))
        }
    }

    fun getTopRated(page: Int): LiveData<Result<TopRate>> = liveData {
        emit(Result.Loading())
        try {
            val value = movieRepository.getTopRateMovies(page)
            emit(Result.Success(value))
        } catch (e: Exception) {
            emit(Result.Error(ResponseError("", 0)))
        }
    }

    fun getMovieDetails(movieId: Int): LiveData<Result<MovieDetail.Movie>> = liveData {
        emit(Result.Loading())
        try {
            val value = movieRepository.getMovieDetail(movieId)
            emit(Result.Success(value))
        } catch (e: Exception) {
            emit(Result.Error(ResponseError("", 0)))
        }
    }

    val nowPlaying: Flow<PagingData<MovieInfoResponse>> = Pager(PagingConfig(20)) {
        MovieDataSource(api)
    }.flow.cachedIn(viewModelScope)

}