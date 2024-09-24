package com.example.note.Tools.date_time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTimeHepler {
    companion object {
        const val NORMAL_DATE_FORMAT = "dd/MM/yyyy"
        const val RESPONSE_DATE_FORMAT = "yyyy-MM-dd"
        val LOCALE = Locale.ENGLISH

        val NORMAL_SDF = SimpleDateFormat(NORMAL_DATE_FORMAT, LOCALE)
        val RESPONSE_SDF = SimpleDateFormat(RESPONSE_DATE_FORMAT, LOCALE)

        fun getCurrentTime(): String? {
             return DateTimeHepler.RESPONSE_SDF.format(System.currentTimeMillis())
        }

        fun reportTimeToDate(time: String?): Date? {
            try {
                time?.let {
                    return RESPONSE_SDF.parse(it)
                } ?: return  null
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}