package com.example.note.Data.repository.impliment

import com.example.note.Data.api_service.CommonApiService
import com.example.note.Data.repository.repo.CommonRepository
import com.example.note.Data.response.CheckRegisterResponse
import com.example.note.Tools.model_hepler.ModelHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CommonRepositoryImpl(
    private val apiService: CommonApiService
): CommonRepository {
    override suspend fun checkRegister(idSinhVien: String): Flow<CheckRegisterResponse?>  = flow {
        try {
            val body = ModelHelper.buildIdSinhVienRequestBody(idSinhVien)
            val result = apiService.checkRegister(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }
}