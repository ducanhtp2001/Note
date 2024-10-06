package com.example.note.Tools.AnotherTools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

object ConvertImg {
    fun Image2String(bitmap: Bitmap?): String {
        val img2String = Img2String()
        try {
            return img2String.execute(bitmap).get()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun String2Image(str: String?): Bitmap {
        val string2Img = String2Img()
        try {
            return string2Img.execute(str).get()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
