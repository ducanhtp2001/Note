package com.example.note.data.model

import java.io.Serializable

class Course(
    var maMon: Int? = -1,
    var tenMon: String? = "",
    var lopTinChi: Int? = -1,
    var soTinChi: Int? = -1,
    var tenGiaoVien: String? = ""
) : Serializable {
    val tenLop: String
        get() = tenMon + " L0" + this.lopTinChi

    override fun toString(): String {
        return (maMon
            .toString() + "/" + this.tenMon
                + "/" + this.lopTinChi
                + "/" + this.soTinChi
                + "/" + this.tenGiaoVien)
    }
}
