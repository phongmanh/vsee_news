package com.manhnguyen.codebase.data.model

import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("status_message") val status_message: String,
    @SerializedName("status_code") val status_code: Int
)
