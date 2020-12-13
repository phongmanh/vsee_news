package com.manhnguyen.codebase

import android.annotation.SuppressLint
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen
import com.manhnguyen.codebase.common.SharedPreferenceHelper
import com.manhnguyen.codebase.di.module.*
import com.manhnguyen.codebase.presentation.UncaughtExceptionHandler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ApplicationController : MultiDexApplication(), LifecycleObserver {

    companion object {
        val instance by lazy { ApplicationController() }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler(this))
        SharedPreferenceHelper.getInstance(this)
        startKoin {
            androidContext(this@ApplicationController)
            modules(
                listOf(
                    ServiceModule.serviceModule,
                    AppModule.appModule,
                    APIServiceModule.apiModule,
                    DatabaseModule.databaseModule,
                    ViewModelModules.modules,
                    RepositoryModules.modules,
                    UseCaseModule.modules
                )
            )

        }
        init()
    }

    @SuppressLint("CheckResult")
    fun init() {


    }


}
