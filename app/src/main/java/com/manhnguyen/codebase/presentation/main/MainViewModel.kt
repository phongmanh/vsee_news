package com.manhnguyen.codebase.presentation.main

import androidx.lifecycle.ViewModel
import com.manhnguyen.codebase.common.SchedulerProvider
import com.manhnguyen.codebase.data.room.databases.AppDatabase
import com.manhnguyen.codebase.di.module.AppModule
import com.manhnguyen.codebase.di.module.DatabaseModule


class MainViewModel(
    private val databaseModule: DatabaseModule,
    private val appModule: AppModule
) :
    ViewModel() {
    private val database: AppDatabase = databaseModule.provideDatabase()
    private val provider: SchedulerProvider = appModule.provideSchedulerProvider()
}