package com.manhnguyen.codebase.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.MovieInfoResponse

class MovieDataSource constructor(private val api: Api) : PagingSource<Int, MovieInfoResponse>() {

    override fun getRefreshKey(state: PagingState<Int, MovieInfoResponse>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieInfoResponse> {
        try {
            val nextPage = params.key ?: 1
            val response = api.movieApi.getNowPlayingPaing(nextPage)
            return LoadResult.Page(
                response.results as List<MovieInfoResponse>,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}