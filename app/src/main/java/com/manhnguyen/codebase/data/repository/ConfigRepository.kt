package com.manhnguyen.codebase.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.Configuration
import com.manhnguyen.codebase.data.model.ResponseError
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.data.room.dao.ConfigDao
import com.manhnguyen.codebase.data.room.databases.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ConfigRepository constructor(private val db: AppDatabase, private val api: Api) {

    private var configDao: ConfigDao = db.configDao()
    private val scope = CoroutineScope(Dispatchers.IO)
    suspend fun fetchConfig() = suspendCoroutine<Result<Any>> { continuation ->
        try {
            api.configApi.fetchConfig().enqueue(object : Callback<Configuration> {
                override fun onResponse(
                    call: Call<Configuration>,
                    response: Response<Configuration>
                ) {
                    if (response.isSuccessful && response.body() != null){
                        configDao.insertOrUpdate(response.body()!!)
                        continuation.resume(Result.Success(true))
                    } else {
                        val result : ResponseError = Gson().fromJson(response.errorBody()!!.string(), object : TypeToken<ResponseError>(){}.type)
                        continuation.resume(Result.Error(result))
                    }

                }

                override fun onFailure(call: Call<Configuration>, t: Throwable) {
                    continuation.resume(handleException(t))
                }
            })

        } catch (e: Exception) {
            continuation.resume(handleException(e))
        }
    }

    private fun handleException(e: Throwable): Result<ResponseError> {
        return try {
            Gson().fromJson(e.message, object : TypeToken<ResponseError>() {}.type)
        } catch (ex: Exception) {
            Result.Error(ResponseError("", 0))
        }
    }
}