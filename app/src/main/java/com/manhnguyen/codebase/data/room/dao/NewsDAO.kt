package com.manhnguyen.codebase.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.manhnguyen.codebase.data.model.News
import java.util.*
import java.util.concurrent.Flow

@Dao
interface NewsDAO : IDao<News> {

    @Query("SELECT * FROM news")
    fun getAll(): PagingSource<Int, News>

    @Query("SELECT * FROM news WHERE newsId =:newsId")
    fun getNewsById(newsId: UUID): kotlinx.coroutines.flow.Flow<News>
}