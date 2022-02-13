package com.manhnguyen.codebase.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.Configuration
import com.manhnguyen.codebase.data.model.ResponseError
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.data.room.databases.AppDatabase

class MapsViewModel constructor(val api: Api, private val db: AppDatabase) : ViewModel() {

    fun getConfig(): LiveData<Result<Configuration>> = liveData {
        emit(Result.Loading())
        Result
        try {
            val config = db.configDao().getConfig()
            emit(Result.Success(config.value!!))
        } catch (ex: Exception) {
            emit(Result.Error(ResponseError("", 0)))
        }

    }

    fun loadConfig(): LiveData<Configuration> = db.configDao().getConfig()
}