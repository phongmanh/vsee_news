package com.manhnguyen.codebase.util

import com.manhnguyen.codebase.data.model.MovieDetail
import com.manhnguyen.codebase.data.model.MovieInfo
import org.threeten.bp.LocalDate

object UIUtils {
    @JvmStatic
    fun numberToString(n: Any): String = "$n"

    @JvmStatic
    fun getDisplayDetailsTitle(info: MovieDetail.Movie?): String = "${info?.title}(${getDisplayYearOfDate(info?.release_date)})"

    @JvmStatic
    fun getDisplayDetailsSubTitle(movie: MovieDetail.Movie?): String {
        var genres = ""
        movie?.genres?.forEach {
            genres = if (genres == "")
                genres.plus(it.name)
            else
                genres.plus(", ${it.name}")
        }

        var duration = ""
        movie?.runtime?.let {
            var strHour = ""
            var strMin = ""
            if (it >= 60) {
                strHour = "${it / 60}h"
                val min = it - ((it / 60) * 60)
                if (min > 0)
                    strMin = "${min}m"

                duration = strHour.plus(strMin)
            } else {
                duration = "${it}m"
            }
        }
        return "${movie?.vote_average}/10 | $duration | $genres"
    }

    @JvmStatic
    fun getDisplayYearOfDate(strDate: String?): String = "${strDate?.let{LocalDate.parse(strDate).year}}"
}