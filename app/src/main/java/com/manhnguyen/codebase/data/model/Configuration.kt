package com.manhnguyen.codebase.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "configuration")
data class Configuration(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @SerializedName("images")
    val images: Image,
    @SerializedName("change_keys")
    val change_keys: List<String>
)