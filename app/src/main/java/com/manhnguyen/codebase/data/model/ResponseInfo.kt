package com.manhnguyen.codebase.data.model

import com.google.gson.annotations.SerializedName

data class ResponseInfo(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)
