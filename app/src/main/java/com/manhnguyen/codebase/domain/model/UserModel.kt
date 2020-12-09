package com.manhnguyen.codebase.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    val Id: Int,
    val userName: String,
    val password: String
)