package com.example.note.data.repository.repo

import com.example.note.data.model.TaiKhoan
import com.example.note.data.response.BaseResponse
import com.example.note.data.response.CheckRegisterResponse
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    suspend fun checkRegister(idSinhVien: String): Flow<CheckRegisterResponse?>
    suspend fun register(taiKhoan: TaiKhoan): Flow<BaseResponse?>
}