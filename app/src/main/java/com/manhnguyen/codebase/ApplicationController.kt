package com.manhnguyen.codebase

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import com.manhnguyen.codebase.common.SharedPreferenceHelper
import com.manhnguyen.codebase.di.*
import com.manhnguyen.codebase.di.*
import com.manhnguyen.codebase.ui.UncaughtExceptionHandler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ApplicationController : MultiDexApplication(), LifecycleObserver {

    companion object {
        val instance by lazy { ApplicationController() }
    }

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler(this))
        SharedPreferenceHelper.getInstance(this)
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@ApplicationController)
            modules(
                listOf(
                    ServiceModule.serviceModule,
                    AppModule.appModule,
                    APIServiceModule.apiModule,
                    DatabaseModule.databaseModule,
                    ViewModelModule.modules,
                    RepositoryModule.modules,
                )
            )

        }
        init()
    }

    @SuppressLint("CheckResult")
    fun init() {


    }


}
