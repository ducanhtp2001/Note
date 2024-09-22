package com.example.note.data.repository.impliment

import com.example.note.data.api_service.CommonApiService
import com.example.note.data.repository.repo.CommonRepository
import com.example.note.data.response.CheckRegisterResponse
import com.example.note.Tools.model_hepler.ModelHelper
import com.example.note.data.AppState
import com.example.note.data.model.ResponseClass
import com.example.note.data.model.ResponseNote
import com.example.note.data.model.ResponseSchedule
import com.example.note.data.model.TaiKhoan
import com.example.note.data.response.BaseResponse
import com.example.note.data.response.LoginResponse
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

    override suspend fun login(
        account: String,
        password: String
    ): Flow<LoginResponse?> = flow {
        try {
            val body = ModelHelper.buildLoginRequestBody(account, password)
            val result = apiService.login(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun getNote(): Flow<ResponseNote?> = flow {
        try {
            val id = AppState.getInstance().getSinhVien().id
            val body = ModelHelper.buildIDRequestBody(id)
            val result = apiService.getNote(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun deleteNote(id: Int): Flow<BaseResponse?> = flow {
        try {
            val idSinhVien = AppState.getInstance().getSinhVien().idStr
            val body = ModelHelper.buildDeleteNoteRequestBody(idSinhVien, id)
            val result = apiService.deleteNote(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun getClassById(): Flow<ResponseClass?> = flow {
        try {
            val id = AppState.getInstance().getSinhVien().id
            val body = ModelHelper.buildIDRequestBody(id)
            val result = apiService.getClassById(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun getSchedules(): Flow<ResponseSchedule?> = flow {
        try {
            val id = AppState.getInstance().getSinhVien().id
            val body = ModelHelper.buildIDRequestBody(id)
            val result = apiService.getSchedules(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

//    override suspend fun getAvatarUrl(idSinhVien: String): Flow<ResponseAvatar?> {
//
//    }
//
//    override suspend fun setAvatarUrl(idSinhVien: String, avatarUrl: String): Flow<BaseResponse?> {
//
//    }

}