package com.example.note.Tools.date_time

import java.text.SimpleDateFormat
import java.util.Locale

class DateTimeHepler {
    companion object {
        const val NORMAL_DATE_FORMAT = "dd/MM/yyyy"
        val LOCALE = Locale.ENGLISH

        val NORMAL_SDF = SimpleDateFormat(NORMAL_DATE_FORMAT, LOCALE)

    }
}