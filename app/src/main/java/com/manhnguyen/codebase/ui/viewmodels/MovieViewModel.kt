package com.manhnguyen.codebase.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.*
import com.manhnguyen.codebase.data.repository.MovieDataSource
import com.manhnguyen.codebase.data.repository.MovieRepository
import com.manhnguyen.codebase.ui.adapters.SimpleRecycleViewPagingAdapter
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerPagingItem
import com.manhnguyen.codebase.ui.adapters.movies.MoviePagingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


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

    fun nowPlaying(adapter: SimpleRecycleViewPagingAdapter): Flow<PagingData<SimpleRecyclerPagingItem>> =
        Pager(PagingConfig(20)) {
            MovieDataSource(api)
        }.flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope).map { pageData ->
            pageData.flatMap { movieInfo ->
                movieInfo.imagePosterUrl = api.imagePosterBaseUrl + "/${movieInfo.poster_path}"
                val item = MoviePagingItem(movieInfo, adapter)
                adapter.viewTypeAndHolder[item.getViewType()] = item.getViewHolderProvider()
                arrayListOf(item)
            }
        }
}