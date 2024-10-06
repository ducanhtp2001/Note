package com.example.note.Tools.model_hepler

import com.example.note.data.model.Course
import com.example.note.data.model.Message
import com.example.note.data.model.Note
import com.example.note.data.model.Post
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

        fun buildEditNoteRequestBody(idSinhVien: Int, note: Note): RequestBody {
            val map = mutableMapOf(
                "idSinhVien" to idSinhVien.toString(),
                "tieuDe" to note.tieuDe.toString(),
                "ngayTao" to note.ngayTao.toString(),
                "ngayCapNhat" to note.ngayCapNhat.toString(),
                "noiDung" to note.noiDung.toString(),
            )
            if (note.noiDungCua != null) {
                map["noiDungCua"] = note.noiDungCua.toString()
            }
            note.id?.let {
                if (it >= 0) map["id"] = it.toString()
            }
            return buildRequestBody(map)
        }

        fun buildEditPostRequestBody(idSinhVien: Int, post: Post): RequestBody {
            val map = mutableMapOf(
                "idSinhVien" to idSinhVien.toString(),
                "thoiGian" to post.thoiGian.toString(),
                "noiDung" to post.noiDung.toString(),
                "coImg" to post.coImg.toString(),
                "img" to post.img.toString(),
            )
            post.id?.let {
                if (it >= 0) map["id"] = it.toString()
            }
            return buildRequestBody(map)
        }

        fun buildEditMessageRequestBody(idSinhVien: Int, post: Message): RequestBody {
            val map = mutableMapOf(
                "idPost" to post.idPost.toString(),
                "idSinhVien" to idSinhVien.toString(),
                "thoiGian" to post.thoiGian.toString(),
                "noiDung" to post.noiDung.toString(),
                "coImg" to post.coImg.toString(),
                "img" to post.img.toString(),
            )
            post.id?.let {
                if (it >= 0) map["id"] = it.toString()
            }
            return buildRequestBody(map)
        }

        fun buildIDRequestBody(id: Int): RequestBody {
            val map = mapOf(
                "id" to id.toString()
            )
            return buildRequestBody(map)
        }

        fun buildDeleteNoteRequestBody(idSinhVien: String, id: Int): RequestBody {
            val map = mapOf(
                "idSinhVien" to idSinhVien,
                "id" to id.toString()
            )
            return buildRequestBody(map)
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

        fun buildStudentsRequestBody(selectedCourse: Course): RequestBody {
            val map = mapOf(
                "maMon" to selectedCourse.maMon.toString(),
                "lopTinChi" to selectedCourse.lopTinChi.toString()
            )
            return buildRequestBody(map)
        }

        fun buildDeleteByIdRequestBody(id: Int): RequestBody {
            val map = mapOf(
                "id" to id.toString()
            )
            return buildRequestBody(map)
        }

        fun buildGetMessagesRequestBody(id: Int): RequestBody {
            val map = mapOf(
                "idPost" to id.toString()
            )
            return buildRequestBody(map)
        }

        fun buildPostRequestBody(selectedCourse: Course): RequestBody {
            val map = mapOf(
                "maMon" to selectedCourse.maMon.toString(),
                "lopTinChi" to selectedCourse.lopTinChi.toString()
            )
            return buildRequestBody(map)
        }
    }
}