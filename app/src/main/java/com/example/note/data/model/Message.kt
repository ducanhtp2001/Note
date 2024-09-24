package com.example.note.data.model

import android.graphics.Bitmap
import com.example.note.Tools.AnotherTools.ConvertImg

class Message(
    var id: Int,
    var idPost: Int,
    var idSinhVien: Int,
    var tenSinhVien: String,
    var thoiGian: String,
    var tinNhan: String,
    var coImg: Int,
    var img: String
) {
    val imgOnBitmap: Bitmap
        get() = ConvertImg.String2Image(this.img)
}
