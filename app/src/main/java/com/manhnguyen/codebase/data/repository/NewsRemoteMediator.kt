package com.manhnguyen.codebase.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.manhnguyen.codebase.data.model.News
import com.manhnguyen.codebase.data.model.RemoteKeys
import com.manhnguyen.codebase.data.room.databases.AppDatabase
import com.manhnguyen.codebase.data.toDataModel
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val database: AppDatabase,
    private val newsRepository: NewsRepository
) : RemoteMediator<Int, News>() {

    private val newsDAO = database.newsDao()
    private val remoteKeysDAO = database.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, News>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                        ?: throw InvalidObjectException("Result is empty")
                    remoteKeys.nextKey ?: return MediatorResult.Success(true)
                }
            }
            val response = newsRepository.getEverything(state.config.pageSize, page)
            val endOfPaginationReached = response.totalResults <= 0
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDAO.delete()
                }
                val newsData = response.toDataModel()
                newsDAO.insert(*response.toDataModel().toTypedArray())
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys =  newsData.map {
                    RemoteKeys(it.newsId, prevKey, nextKey)
                }.toTypedArray()
                remoteKeysDAO.insert(*remoteKeys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, News>): RemoteKeys? {
        return state.lastItemOrNull()?.let { news ->
            database.withTransaction { database.remoteKeysDao().remoteKeysByNewsId(news.newsId) }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, News>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.newsId?.let { id ->
                database.withTransaction { database.remoteKeysDao().remoteKeysByNewsId(id) }
            }
        }
    }
}
