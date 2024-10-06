package com.example.note.data.repository.repo

import com.example.note.UI.School.model.MessageResponse
import com.example.note.UI.School.model.PostResponse
import com.example.note.data.model.Message
import com.example.note.data.model.Note
import com.example.note.data.model.Post
import com.example.note.data.model.ResponseClass
import com.example.note.data.model.ResponseNote
import com.example.note.data.model.ResponseSchedule
import com.example.note.data.model.ResponseSinhVien
import com.example.note.data.model.TaiKhoan
import com.example.note.data.response.BaseResponse
import com.example.note.data.response.CheckRegisterResponse
import com.example.note.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface CommonRepository {
    suspend fun checkRegister(idSinhVien: String): Flow<CheckRegisterResponse?>
    suspend fun register(taiKhoan: TaiKhoan): Flow<BaseResponse?>
    suspend fun login(account: String, password: String): Flow<LoginResponse?>

    suspend fun getNote(): Flow<ResponseNote?>
    suspend fun editNote(note: Note): Flow<BaseResponse?>
    suspend fun deleteNote(id: Int): Flow<BaseResponse?>
    suspend fun getClassById(): Flow<ResponseClass?>
    suspend fun getSchedules(): Flow<ResponseSchedule?>
    suspend fun getStudents(): Flow<ResponseSinhVien?>
    suspend fun getPosts(): Flow<PostResponse?>
    suspend fun deletePost(id: Int): Flow<BaseResponse?>
    suspend fun getMessages(): Flow<MessageResponse?>
    suspend fun deleteMessage(id: Int): Flow<BaseResponse?>
    suspend fun editMessage(message: Message): Flow<BaseResponse?>
    suspend fun editPost(post: Post): Flow<BaseResponse?>


//    suspend fun getAvatarUrl(idSinhVien: String): Flow<ResponseAvatar?>
//    suspend fun setAvatarUrl(idSinhVien: String, avatarUrl: String): Flow<BaseResponse?>

}