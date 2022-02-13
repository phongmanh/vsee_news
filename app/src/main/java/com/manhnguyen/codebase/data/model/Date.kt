package com.manhnguyen.codebase.data.model

import com.google.gson.annotations.SerializedName

data class Date (
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String)