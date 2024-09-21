package com.example.note.Tools.model_hepler

import com.example.note.Data.model.SinhVien
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlin.math.sinh

class ModelHelper {
    companion object {

        fun buildIdSinhVienRequestBody(idSinhVienStr: String): RequestBody {
            val sinhView = SinhVien()
            sinhView.idStr = idSinhVienStr

            return MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("idSinhVien", sinhView.id.toString())
                .addFormDataPart("khoa", sinhView.khoaStr)
                .addFormDataPart("nienKhoa", sinhView.nienKhoaStr)
                .addFormDataPart("lop", sinhView.lopStr)
                .build()
        }



    }
}