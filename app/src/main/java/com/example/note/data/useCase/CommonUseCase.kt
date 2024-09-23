package com.example.note.data.useCase

import com.example.note.data.model.Note
import com.example.note.data.model.TaiKhoan
import com.example.note.data.repository.repo.CommonRepository
import javax.inject.Inject

class CommonUseCase @Inject constructor(private val repository: CommonRepository) {
    suspend fun checkRegister(idSinhVien: String) = repository.checkRegister(idSinhVien)
    suspend fun register(taiKhoan: TaiKhoan) = repository.register(taiKhoan)
    suspend fun login(account: String, password: String) = repository.login(account, password)
    suspend fun getNoteById() = repository.getNote()
    suspend fun editNote(note: Note) = repository.editNote(note)
    suspend fun getClassById() = repository.getClassById()
    suspend fun getSchedules() = repository.getSchedules()
    suspend fun deleteNote(noteId: Int) = repository.deleteNote(noteId)

//    suspend fun saveNote(note: Note) = repository.saveNote(note)
//    suspend fun deleteNote(id: Int) = repository.deleteNote(id)

}