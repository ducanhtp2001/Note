package com.example.note.data.api_service

import com.example.note.uI.School.model.MessageResponse
import com.example.note.uI.School.model.PostResponse
import com.example.note.data.model.ResponseClass
import com.example.note.data.model.ResponseNote
import com.example.note.data.model.ResponseSchedule
import com.example.note.data.model.ResponseSinhVien
import com.example.note.data.response.BaseResponse
import com.example.note.data.response.CheckRegisterResponse
import com.example.note.data.response.LoginResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface CommonApiService {

    @POST("checkRegister.php")
    suspend fun checkRegister(@Body request: RequestBody): CheckRegisterResponse?

    @POST("register.php")
    suspend fun register(@Body request: RequestBody): BaseResponse?

    @POST("login.php")
    suspend fun login(@Body request: RequestBody): LoginResponse?

    @POST("getNote.php")
    suspend fun getNote(@Body request: RequestBody): ResponseNote?

    @POST("deleteNote.php")
    suspend fun deleteNote(@Body request: RequestBody): BaseResponse?

    @POST("insertNote.php")
    suspend fun editNote(@Body request: RequestBody): BaseResponse?

    @POST("getClass.php")
    suspend fun getClassById(@Body request: RequestBody): ResponseClass?

    @POST("getCalendar.php")
    suspend fun getSchedules(@Body request: RequestBody): ResponseSchedule?

    @POST("getStudents.php")
    suspend fun getStudents(@Body request: RequestBody): ResponseSinhVien?

    @POST("getPost.php")
    suspend fun getPosts(@Body request: RequestBody): PostResponse?

    @POST("insertPost.php")
    suspend fun editPosts(@Body request: RequestBody): BaseResponse?

    @POST("deletePost.php")
    suspend fun deletePost(@Body request: RequestBody): BaseResponse?

    @POST("getMessage.php")
    suspend fun getMessages(@Body request: RequestBody): MessageResponse?

    @POST("insertMessage.php")
    suspend fun editMessage(@Body request: RequestBody): BaseResponse?

    @POST("deleteMessage.php")
    suspend fun deleteMessage(@Body request: RequestBody): BaseResponse?


//    @POST("getAvatarUrl.php")
//    suspend fun getAvatarUrl(@Body request: RequestBody): ResponseAvatar?
//
//    @POST("setAvatarUrl.php")
//    suspend fun setAvatarUrl(@Body request: RequestBody): BaseResponse?
}