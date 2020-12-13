package com.manhnguyen.codebase.di.module

import android.content.Context
import androidx.room.Room
import com.manhnguyen.codebase.data.room.databases.AppDatabase
import com.manhnguyen.codebase.data.room.databases.DataMigration
import org.koin.dsl.module


class DatabaseModule constructor(private val context: Context) {

    companion object {
        val databaseModule = module {
            single { DatabaseModule(get()) }
        }
    }

    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "gpstrackingdb")
            .allowMainThreadQueries()
            .addMigrations(
                DataMigration().migration_1_2,
                DataMigration().migration_2_3,
                DataMigration().migration_3_4,
                DataMigration().migration_4_5
            )
            .build()
    }

}