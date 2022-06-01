package com.manhnguyen.codebase.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

object UIUtils {
    @JvmStatic
    fun numberToString(n: Any): String = "$n"

    @JvmStatic
    fun getDisplayYearOfDate(strDate: String?): String = ""

    @JvmStatic
    fun getDisplayDateTime(strDate: String): String {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val dateTime = sdf.parse(strDate)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(dateTime)
    }

}