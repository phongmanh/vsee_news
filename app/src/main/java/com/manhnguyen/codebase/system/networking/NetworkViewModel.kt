package com.manhnguyen.codebase.system.networking

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.github.pwittchen.reactivenetwork.library.rx2.internet.observing.InternetObservingSettings
import com.manhnguyen.codebase.common.SchedulerProvider


class NetworkViewModel constructor(private val schedulerProvider: SchedulerProvider) : ViewModel() {

    private val internetConnected = MutableLiveData<Boolean>()

    init {
        initInternetListener()
    }

    fun getInternetState(): LiveData<Boolean> {
        return internetConnected
    }

    fun getIsConnected(): Boolean {
        return internetConnected.value ?: false
    }

    @SuppressLint("CheckResult")
    private fun initInternetListener() {
        try {
            val settings = InternetObservingSettings.builder()
                .interval(1000)
                .build()

            ReactiveNetwork.observeInternetConnectivity(settings)
                .observeOn(schedulerProvider.mainThread)
                .subscribeOn(schedulerProvider.ioScheduler)
                .subscribe {
                    if (internetConnected.value != it)
                        internetConnected.postValue(it)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}