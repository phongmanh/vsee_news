package com.manhnguyen.codebase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val newsId: UUID,
    val prevKey: Int?,
    val nextKey: Int?
)