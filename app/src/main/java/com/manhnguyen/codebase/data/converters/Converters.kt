package com.manhnguyen.codebase.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.manhnguyen.codebase.data.model.Image
import com.manhnguyen.codebase.data.model.MovieDetail
import com.manhnguyen.codebase.data.model.MovieInfo
import com.manhnguyen.codebase.data.model.ResponseInfo

class EntityConverters {

    @TypeConverter
    fun listMovieInfoFromString(s: String): List<MovieInfo> =
        Gson().fromJson(s, object : TypeToken<List<MovieInfo>>() {}.type)

    @TypeConverter
    fun listMovieInfoToString(movies: List<MovieInfo>): String = Gson().toJson(movies)

    @TypeConverter
    fun movieInfoFromString(s: String): MovieInfo = Gson().fromJson(s, object : TypeToken<MovieInfo>() {}.type)

    @TypeConverter
    fun movieInfoToString(movies: MovieInfo): String = Gson().toJson(movies)

    @TypeConverter
    fun productionCompanyFromString(s: String): List<MovieDetail.ProductionCompany> =
        Gson().fromJson(s, object : TypeToken<List<MovieDetail.ProductionCompany>>() {}.type)

    @TypeConverter
    fun productionCompanyToString(productionCompanies: List<MovieDetail.ProductionCompany>): String =
        Gson().toJson(productionCompanies)

    @TypeConverter
    fun productionCountryFromString(s: String): List<MovieDetail.ProductionCountry> =
        Gson().fromJson(s, object : TypeToken<List<MovieDetail.ProductionCountry>>() {}.type)

    @TypeConverter
    fun productionCountryToString(productionCountries: List<MovieDetail.ProductionCountry>): String =
        Gson().toJson(productionCountries)

    @TypeConverter
    fun spokenLangToString(s: String): List<MovieDetail.SpokenLanguage> =
        Gson().fromJson(s, object : TypeToken<List<MovieDetail.SpokenLanguage>>() {}.type)

    @TypeConverter
    fun spokenLangFromString(spokenLangs: List<MovieDetail.SpokenLanguage>): String =
        Gson().toJson(spokenLangs)

    @TypeConverter
    fun genresLangToString(s: String): List<MovieDetail.Genres> =
        Gson().fromJson(s, object : TypeToken<List<MovieDetail.Genres>>() {}.type)

    @TypeConverter
    fun genresLangFromString(genres: List<MovieDetail.Genres>): String =
        Gson().toJson(genres)

    @TypeConverter
    fun imageFromString(s: String): Image = Gson().fromJson(s, object : TypeToken<Image>() {}.type)

    @TypeConverter
    fun imageToString(image: Image): String = Gson().toJson(image)


}