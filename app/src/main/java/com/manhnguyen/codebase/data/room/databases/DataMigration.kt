package com.manhnguyen.codebase.data.room.databases

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


class DataMigration {

    val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {

        }
    }

}