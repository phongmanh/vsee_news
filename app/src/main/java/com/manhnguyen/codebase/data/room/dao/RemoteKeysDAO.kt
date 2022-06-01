package com.manhnguyen.codebase.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.manhnguyen.codebase.data.model.RemoteKeys
import java.util.*

@Dao
interface RemoteKeysDAO: IDao<RemoteKeys> {
    @Query("SELECT * FROM remote_keys WHERE newsId = :newsId")
    fun remoteKeysByNewsId(newsId: UUID): RemoteKeys?
}