package com.example.note.Data.repository.repo

import com.example.note.Data.response.CheckRegisterResponse
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    suspend fun checkRegister(idSinhVien: String): Flow<CheckRegisterResponse?>
}