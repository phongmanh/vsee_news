package com.manhnguyen.codebase.data.converters

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class DateConverter {
    @TypeConverter
    fun dateTimeFromString(s: String?): LocalDate? = s?.let { LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE) }

    @TypeConverter
    fun dateTimeToString(dateTime: LocalDate?): String? = dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE)

}