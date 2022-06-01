package com.manhnguyen.codebase.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kwabenaberko.newsapilib.models.Article
import com.manhnguyen.codebase.common.Configs
import com.manhnguyen.codebase.data.model.News
import com.manhnguyen.codebase.data.toDataModel
import retrofit2.HttpException
import java.io.IOException

class NewsDataSource constructor(private val repository: NewsRepository) :
    PagingSource<Int, News>() {

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        return try {
            val pageIndex = params.key ?: 1
            val response = repository.getEverything(Configs.PAGE_SIZE, pageIndex)
            val results = response.articles
            val nextKey = if (results == null) null else {
                pageIndex + (params.loadSize / 20)
            }
            LoadResult.Page(
                response.toDataModel(),
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