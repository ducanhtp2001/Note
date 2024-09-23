package com.example.note.data.model

import com.example.note.Tools.date_time.DateTimeHepler
import com.google.gson.annotations.SerializedName
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

data class Note(
    @SerializedName("id")
    var id: Int? = -1,

    @SerializedName("tieuDe")
    var tieuDe: String? = "",

    @SerializedName("ngayTao")
    var ngayTao: String? = "",

    @SerializedName("ngayCapNhat")
    var ngayCapNhat: String? = "",

    @SerializedName("noiDung")
    var noiDung: String? = "",

    @SerializedName("noiDungCua")
    var noiDungCua: String? = ""
) {
    var ngayTaoDate: Date? = null
    var ngayCapNhatDate: Date? = null
    var noiDungStr: String = ""
    var noiDungCuaStr: String = ""

    init {
        ngayTao?.let { this.ngayTaoDate = convertStringToDate(it) }
        ngayCapNhat?.let { this.ngayCapNhatDate = convertStringToDate(it) }
        this.noiDung = noiDung ?: ""
        this.noiDungCua = noiDungCua ?: ""
    }

    private fun convertStringToDate(dateString: String): Date? {
        try {
            return DateTimeHepler.RESPONSE_SDF.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

    private fun convertStringToDateVer2(dateString: String): Date? {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")

        try {
            return dateFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        @JvmStatic
        fun getNgayStr(date: Date?): String? {
            val outputFormat = SimpleDateFormat("yyyy/MM/dd")

            try {
                val dateStr = outputFormat.format(date)
                return dateStr
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null // Xử lý lỗi hoặc trả về giá trị mặc định theo ý bạn
        }
    }
}
