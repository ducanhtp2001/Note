package com.example.note.data.provider

import com.example.note.data.api_service.CommonApiService
import retrofit2.Retrofit

object ApiServiceProvider {
    fun getCommonApiService(retrofit: Retrofit): CommonApiService {
        return retrofit.create(CommonApiService::class.java)
    }
}