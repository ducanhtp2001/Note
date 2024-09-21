package com.example.note.Data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SinhVien(
    var id: Int = -1,
    var hoTen: String = "",
    var ngaySinh: Date = Date(),
    var gioiTinh: String = "",
    var queQuan: String = "",
    var gmail: String = "",
    var sdt: String = "",
    var khoa: Int = -1,
    var nienKhoa: Int = -1,
    var lop: Int = -1
) {

    var idStr: String = ""
        set(value) {
            field = value
            this.id = getIdFromMaSinhVien(value)
            this.khoa = getKhoaFromMaSinhVien(value)
            this.nienKhoa = getNienKhoaFromMaSinhVien(value)
            this.lop = getLopFromMaSinhVien(value)
        }

    val ngaySinhStr: String
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            return dateFormat.format(this.ngaySinh)
        }

    val gioiTinhBoolean: Boolean
        get() = this.gioiTinh == "Nam"

    val khoaStr: String
        get() {
            when (this.khoa) {
                1 -> return "CNTT"
                2 -> return "ATTT"
                3 -> return "DTVT"
            }
            return ""
        }

    val nienKhoaStr: String
        get() {
            val start = this.nienKhoa + 2015
            val end = this.nienKhoa + 2015 + 5

            return "$start - $end"
        }

    val lopStr: String
        get() {
            var lopStr = ""
            var khoaStr = ""
            if (this.lop == 1) lopStr = "A"
            if (this.lop == 2) lopStr = "B"
            if (this.lop == 3) lopStr = "C"
            if (this.lop == 4) lopStr = "D"

            if (this.khoa == 1) khoaStr = "CT"
            if (this.khoa == 2) khoaStr = "AT"
            if (this.khoa == 3) khoaStr = "DT"

            return "" + khoaStr + this.nienKhoa + lopStr
        }

    val maSinhVien: String
        get() {
            var khoa1 = ""
            if (this.khoa == 1) khoa1 = "CT"
            else if (this.khoa == 2) khoa1 = "AT"
            else if (this.khoa == 3) khoa1 = "DT"
            return khoa1 + "0" + nienKhoa.toString() + "0" + lop.toString() + "0" + this.id
        }

    companion object {
        fun getIdFromMaSinhVien(maSinhVien: String): Int {
            var id = ""
            for (i in 6 until maSinhVien.length) {
                id = id + maSinhVien[i]
            }
            return id.toInt()
        }

        fun getKhoaFromMaSinhVien(maSinhVien: String): Int {
            var khoa = ""
            for (i in 0..1) {
                khoa = khoa + maSinhVien[i]
            }
            khoa = khoa.uppercase(Locale.getDefault())
            if ((khoa[0] == 'c' || khoa[0] == 'C') && (khoa[1] == 't' || khoa[1] == 'T')) return 1
            if ((khoa[0] == 'a' || khoa[0] == 'A') && (khoa[1] == 't' || khoa[1] == 'T')) return 2
            if ((khoa[0] == 'd' || khoa[0] == 'D') && (khoa[1] == 't' || khoa[1] == 'T')) return 3
            return -1
        }

        fun getNienKhoaFromMaSinhVien(maSinhVien: String): Int {
            var nienKhoa = ""
            for (i in 2..3) {
                nienKhoa = nienKhoa + maSinhVien[i]
            }

            return nienKhoa.toInt()
        }

        fun getLopFromMaSinhVien(maSinhVien: String): Int {
            var lop = ""
            for (i in 4..5) {
                lop = lop + maSinhVien[i]
            }

            return lop.toInt()
        }
    }
}
