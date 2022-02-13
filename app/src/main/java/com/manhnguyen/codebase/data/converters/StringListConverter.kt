package com.manhnguyen.codebase.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    @TypeConverter
    fun toListString(s: String): List<String> =
        Gson().fromJson(s, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun fromListString(stringList: List<String>): String = Gson().toJson(stringList)
}