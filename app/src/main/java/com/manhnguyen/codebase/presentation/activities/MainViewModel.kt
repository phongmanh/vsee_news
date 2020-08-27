package com.manhnguyen.codebase.presentation.activities

import androidx.lifecycle.*
import com.manhnguyen.codebase.common.SchedulerProvider
import com.manhnguyen.codebase.data.room.databases.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var provider: SchedulerProvider

}