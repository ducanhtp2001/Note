package com.example.note.data.useCase

import com.example.note.data.model.TaiKhoan
import com.example.note.data.repository.repo.CommonRepository
import javax.inject.Inject

class CommonUseCase @Inject constructor(private val repository: CommonRepository) {
    suspend fun checkRegister(idSinhVien: String) = repository.checkRegister(idSinhVien)
    suspend fun register(taiKhoan: TaiKhoan) = repository.register(taiKhoan)
    suspend fun login(account: String, password: String) = repository.login(account, password)

}