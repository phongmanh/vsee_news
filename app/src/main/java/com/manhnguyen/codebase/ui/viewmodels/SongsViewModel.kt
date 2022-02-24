package com.manhnguyen.codebase.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.manhnguyen.codebase.data.model.ResponseError
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.data.model.SongInfo
import com.manhnguyen.codebase.data.repository.SongRepository

class SongsViewModel(private val songRepository: SongRepository) : ViewModel() {

    fun getSongs(): LiveData<Result<List<SongInfo>>> = liveData {
        emit(Result.Loading())
        try {
            val song = songRepository.getSongs()
            emit(Result.Success(song))
        } catch (e: Exception) {
            emit(Result.Error(ResponseError("", 0)))
        }
    }

}