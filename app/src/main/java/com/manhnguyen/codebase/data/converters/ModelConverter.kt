package com.manhnguyen.codebase.data.converters

import androidx.room.TypeConverter
import androidx.room.util.UUIDUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.Source
import java.util.*

class ModelConverter {

    @TypeConverter
    fun articleFromString(s: String): Article = Gson().fromJson(s, object : TypeToken<Article>() {}.type)

    @TypeConverter
    fun articleToString(news: Article): String = Gson().toJson(news)

    @TypeConverter
    fun sourceFromString(s: String): Source = Gson().fromJson(s, object : TypeToken<Source>() {}.type)

    @TypeConverter
    fun sourceToString(source: Source): String = Gson().toJson(source)

    @TypeConverter
    fun uuidFromString(s: String): UUID = UUID.fromString(s)

    @TypeConverter
    fun uuidToString(uuid: UUID):String = uuid.toString()

}