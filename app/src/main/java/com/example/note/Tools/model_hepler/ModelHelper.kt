package com.example.note.Tools.model_hepler

import com.example.note.data.model.SinhVien
import com.example.note.data.model.TaiKhoan
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody

class ModelHelper {
    companion object {

        private val gson: Gson = GsonBuilder().create()
        private val mediaType: MediaType =  "application/json; charset=utf-8".toMediaType()

        fun buildIdSinhVienRequestBody(idSinhVienStr: String): RequestBody {
            val sinhView = SinhVien()
            sinhView.idStr = idSinhVienStr

            val data = mapOf(
                "idSinhVien" to sinhView.id.toString(),
                "khoa" to sinhView.khoa.toString(),
                "nienKhoa" to sinhView.nienKhoa.toString(),
                "lop" to sinhView.lop.toString()
            )

            val json = gson.toJson(data)
            return RequestBody.create(mediaType, json)
        }

        fun buildLoginRequestBody(account: String, password: String): RequestBody {
            val map = mapOf(
                "idSinhVien" to account,
                "password" to password
            )
            return buildRequestBody(map)
        }

        fun buildRegisterRequestBody(taiKhoan: TaiKhoan): RequestBody {
            val json = gson.toJson(taiKhoan)
            return RequestBody.create(mediaType, json)
        }

        private fun buildRequestBody(map: Map<String, String>): RequestBody {
            val json = gson.toJson(map)
            return RequestBody.create(mediaType, json)
        }
    }
}