package com.manhnguyen.codebase.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntListConverter {

    @TypeConverter
    fun toIntList(s: String): List<Int> =
        Gson().fromJson(s, object : TypeToken<List<Int>>() {}.type)

    @TypeConverter
    fun fromIntList(intList: List<Int>): String = Gson().toJson(intList)
}