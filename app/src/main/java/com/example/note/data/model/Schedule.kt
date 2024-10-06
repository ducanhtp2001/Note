package com.example.note.data.model

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

data class Schedule(
    var maMon: Int? = null,
    var tenMon: String? = "",
    var soTinChi: Int? = null,
    var lopTinChi: Int? = null,
    var ngayHoc: String? = "",
    var caHoc: Int? = null,
    var phongHoc: String? = ""
) {

    val thoiGianHoc: String
        get() {
            when (this.caHoc) {
                1 -> {
                    return "7h - 9h25"
                }

                2 -> {
                    return "9h35 - 12h AM"
                }

                3 -> {
                    return "12h30 - 14h55"
                }

                4 -> {
                    return "15h05 - 17h30"
                }

                5 -> {
                    return "18h - 21h15 "
                }

            }
            return ""
        }
}
