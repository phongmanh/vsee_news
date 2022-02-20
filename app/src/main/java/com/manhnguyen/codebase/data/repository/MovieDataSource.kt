package com.manhnguyen.codebase.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.MovieInfo
import com.manhnguyen.codebase.data.model.MovieInfoResponse
import com.manhnguyen.codebase.data.model.NowPlaying
import retrofit2.HttpException
import java.io.IOException

class MovieDataSource constructor(private val api: Api) : PagingSource<Int, MovieInfo>() {

    init {
        println(api.imageBackDropBaseUrl)
    }
    override fun getRefreshKey(state: PagingState<Int, MovieInfo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieInfo> {
        return try {
            val pageIndex = params.key ?: 1
            val response = api.movieApi.getNowPlayingPaging(pageIndex)
            val movies = response.results
            val nextKey = if (movies == null) null else {
                pageIndex + (params.loadSize / 20)
            }
            LoadResult.Page(
                response.results,
                prevKey = if (pageIndex == 1) null else pageIndex,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}