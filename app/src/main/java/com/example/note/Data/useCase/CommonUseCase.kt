package com.example.note.Data.useCase

import com.example.note.Data.repository.repo.CommonRepository
import javax.inject.Inject

class CommonUseCase @Inject constructor(private val repository: CommonRepository) {
    suspend fun checkRegister(idSinhVien: String) = repository.checkRegister(idSinhVien)
}