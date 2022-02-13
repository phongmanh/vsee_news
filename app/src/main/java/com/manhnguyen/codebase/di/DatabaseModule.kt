package com.manhnguyen.codebase.di

import android.content.Context
import androidx.room.Room
import com.manhnguyen.codebase.data.room.databases.AppDatabase
import org.koin.dsl.module


class DatabaseModule constructor(private val context: Context) {

    companion object {
        val databaseModule = module {
            single { provideDatabase(get()) }
        }

        private fun provideDatabase(context: Context): AppDatabase {
            return AppDatabase.getInstance(context)
        }
    }


}