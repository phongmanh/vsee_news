package com.manhnguyen.codebase.data.room.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manhnguyen.codebase.data.converters.DateConverter
import com.manhnguyen.codebase.data.converters.EntityConverters
import com.manhnguyen.codebase.data.converters.IntListConverter
import com.manhnguyen.codebase.data.converters.StringListConverter
import com.manhnguyen.codebase.data.room.dao.ConfigDao
import com.manhnguyen.codebase.data.room.dao.MovieDAO
import com.manhnguyen.codebase.data.room.dao.NowPlayingDao
import com.manhnguyen.codebase.data.room.dao.TopRateDao
import com.manhnguyen.codebase.data.room.databases.AppDatabase.Companion.DB_VERSION
import com.manhnguyen.codebase.data.model.*


@Database(
    entities = [MovieDetail.Movie::class, NowPlaying::class, Configuration::class, TopRate::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(
    StringListConverter::class,
    IntListConverter::class,
    DateConverter::class,
    EntityConverters::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDAO

    abstract fun nowPlayingDao(): NowPlayingDao

    abstract fun topRateDao(): TopRateDao

    abstract fun configDao(): ConfigDao

    companion object : SingletonProvider<AppDatabase, Context> {
        const val DB_NAME = "movie_database"
        const val DB_VERSION = 1
        override val singletonInstance = SingletonInstance<AppDatabase, Context> {
            Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()
                .build()
        }

    }

}

interface SingletonProvider<out T, in A> {
    val singletonInstance: SingletonInstance<T, A>
    fun getInstance(arg: A) = singletonInstance.getInstance(arg)
}

class SingletonInstance<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}