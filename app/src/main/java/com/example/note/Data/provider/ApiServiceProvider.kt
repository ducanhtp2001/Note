package com.example.note.Data.provider

import com.example.note.Data.api_service.CommonApiService
import retrofit2.Retrofit

object ApiServiceProvider {
    fun getCommonApiService(retrofit: Retrofit): CommonApiService {
        return retrofit.create(CommonApiService::class.java)
    }
}