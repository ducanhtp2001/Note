package com.example.note.data.model

import android.graphics.Bitmap
import com.example.note.Tools.AnotherTools.ConvertImg

data class Post(
    var id: Int? = null,
    var idSinhVien: Int? = null,
    var thoiGian: String? = "",
    var noiDung: String? = "",
    var coImg: Int? = 0,
    var img: String? = "",
    var maMon: Int? = -1,
    var lopTinChi: Int? = -1,
) {
    val imgOnBitmap: Bitmap?
        get() = if (coImg == 1) {
            ConvertImg.String2Image(this.img)
        } else null
}
