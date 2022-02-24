package com.manhnguyen.codebase.data.repository

import com.manhnguyen.codebase.data.api.Api
import com.manhnguyen.codebase.data.model.SongInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SongRepository(
    private val api: Api,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val songApi = api.songApi

    suspend fun getSongs(): List<SongInfo> = suspendCoroutine { cont ->
        try {
            CoroutineScope(ioDispatcher).launch {
                val songs = songApi.getSongs()
                cont.resume(songs)
            }
        } catch (e: Exception) {
            cont.resumeWithException(e)
        }
    }

}