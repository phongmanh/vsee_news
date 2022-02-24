package com.codebase

import androidx.lifecycle.liveData
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test

class Test {

    private val dispartcher = StandardTestDispatcher()

    @Test
    fun coroutines() = runBlocking {
        getFlow().collect {
            println(it)
        }
    }

    private fun getFlow() = flow {
        for (i in 0..10) {
            emit(i)
        }
    }

    fun getLiveData() = liveData {
        for (i in 0..10) {
            emit(i)
        }
    }
}