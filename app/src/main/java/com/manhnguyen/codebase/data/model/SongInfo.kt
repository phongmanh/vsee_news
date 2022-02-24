package com.manhnguyen.codebase.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["Id"])
data class SongInfo(
    val name: String,
    val Id: Int
)

