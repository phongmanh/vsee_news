package com.manhnguyen.codebase.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.manhnguyen.codebase.data.model.Configuration

@Dao
interface ConfigDao : IDao<Configuration> {
    @Query("SELECT * FROM CONFIGURATION")
    fun getConfig(): LiveData<Configuration>
}