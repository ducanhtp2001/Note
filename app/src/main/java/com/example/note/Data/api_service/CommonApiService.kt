package com.example.note.Data.api_service

import com.example.note.Data.model.ResponseNote
import com.example.note.Data.response.CheckRegisterResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CommonApiService {
    @POST("getNote.php")
    suspend fun getNote(@Body request: RequestBody): Response<ResponseNote>

    @POST("checkRegister.php")
    suspend fun checkRegister(@Body request: RequestBody): CheckRegisterResponse?

}