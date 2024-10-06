package com.example.note.data.model

import android.graphics.Bitmap
import com.example.note.Tools.AnotherTools.ConvertImg

class Message(
    var id: Int? = null,
    var idPost: Int? = null,
    var idSinhVien: Int? = null,
    var tenSinhVien: String? = "",
    var thoiGian: String? = "",
    var noiDung: String? = "",
    var coImg: Int? = 0,
    var img: String? = ""
) {
    val imgOnBitmap: Bitmap
        get() = ConvertImg.String2Image(this.img)
}
