package com.manhnguyen.codebase.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

@Entity(tableName = "top_rate", primaryKeys = ["page"])
data class TopRate(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int,
    @SerializedName("results")
    val results: List<MovieInfo>
)
