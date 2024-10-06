package com.example.note.data.model

import android.graphics.Bitmap
import com.example.note.Tools.AnotherTools.ConvertImg

data class Message(
    var id: Int? = null,
    var idPost: Int? = null,
    var idSinhVien: Int? = null,
    var thoiGian: String? = "",
    var noiDung: String? = "",
    var coImg: Int? = 0,
    var img: String? = ""
) {
    val imgOnBitmap: Bitmap
        get() = ConvertImg.String2Image(this.img)
}
