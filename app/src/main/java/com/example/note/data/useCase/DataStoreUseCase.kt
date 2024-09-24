package com.example.note.data.useCase

import com.example.note.data.model.TaiKhoan
import com.example.note.data.repository.repo.DataStorePreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreUseCase @Inject constructor(
    private val preferencesRepository: DataStorePreferencesRepository
) {
    // MARK: preference
    fun isFirstTimeLaunch(): Flow<Boolean> {
        return preferencesRepository.isFirstTimeLaunch()
    }

    suspend fun updateFirstTimeLaunch(isFirstTimeLaunch: Boolean) {
        preferencesRepository.updateFirstTimeLaunch(isFirstTimeLaunch)
    }

    fun getLastLogin(): Flow<TaiKhoan?> {
        return preferencesRepository.getLastLogin()
    }

    suspend fun setLastLogin(taiKhoan: TaiKhoan) {
        preferencesRepository.setLastLogin(taiKhoan)
    }

}