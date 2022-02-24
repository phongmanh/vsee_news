package com.manhnguyen.codebase.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.Configuration
import com.manhnguyen.codebase.data.model.ResponseError
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.data.repository.ConfigRepository
import com.manhnguyen.codebase.data.room.databases.AppDatabase
import com.manhnguyen.codebase.util.LocationUtils
import com.manhnguyen.codebase.util.PermissionUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class MapsViewModel constructor(
    val api: Api,
    private val configRepository: ConfigRepository,
    private val db: AppDatabase
) : ViewModel() {

    fun getConfig(): LiveData<Result<Configuration>> = liveData {
        emit(Result.Loading())
        Result
        try {
            val result = configRepository.fetchConfig()
            val config = db.configDao().getConfig()
            emit(Result.Success(config.value!!))
        } catch (ex: Exception) {
            emit(Result.Error(ResponseError("", 0)))
        }

    }

    fun loadConfig(): LiveData<Configuration> = db.configDao().getConfig()

    @ExperimentalCoroutinesApi
    fun requestLocationPermission(context: Context) =
        PermissionUtils.dexterRequestPermissions(context, PermissionUtils.LOCATION_PERMISSIONS)
            .catch { e ->
                println(e)
            }.asLiveData()

}