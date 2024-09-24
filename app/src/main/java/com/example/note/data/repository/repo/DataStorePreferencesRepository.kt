package com.example.note.data.repository.repo

import com.example.note.data.model.TaiKhoan
import kotlinx.coroutines.flow.Flow

interface DataStorePreferencesRepository {

    // Onboarding
    fun isFirstTimeLaunch(): Flow<Boolean>
    suspend fun updateFirstTimeLaunch(isFirstTimeLaunch: Boolean)

    fun getLastLogin(): Flow<TaiKhoan?>
    suspend fun setLastLogin(taiKhoan: TaiKhoan)
}