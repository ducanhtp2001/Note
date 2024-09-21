package com.example.note.data.repository.impliment

import com.example.note.data.api_service.CommonApiService
import com.example.note.data.repository.repo.CommonRepository
import com.example.note.data.response.CheckRegisterResponse
import com.example.note.Tools.model_hepler.ModelHelper
import com.example.note.data.model.TaiKhoan
import com.example.note.data.response.BaseResponse
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

    override suspend fun register(
        taiKhoan: TaiKhoan
    ): Flow<BaseResponse?> = flow {
        try {
            val body = ModelHelper.buildRegisterRequestBody(taiKhoan)
            val result = apiService.register(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }
}