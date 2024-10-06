package com.example.note.data.repository.impliment

import com.example.note.Tools.AnotherTools.guard
import com.example.note.data.api_service.CommonApiService
import com.example.note.data.repository.repo.CommonRepository
import com.example.note.data.response.CheckRegisterResponse
import com.example.note.Tools.model_hepler.ModelHelper
import com.example.note.uI.School.model.MessageResponse
import com.example.note.uI.School.model.PostResponse
import com.example.note.data.AppState
import com.example.note.data.model.Message
import com.example.note.data.model.Note
import com.example.note.data.model.Post
import com.example.note.data.model.ResponseClass
import com.example.note.data.model.ResponseNote
import com.example.note.data.model.ResponseSchedule
import com.example.note.data.model.ResponseSinhVien
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

    override suspend fun editNote(note: Note): Flow<BaseResponse?> = flow {
        try {
            val idSinhVien = AppState.getInstance().getSinhVien().id
            val body = ModelHelper.buildEditNoteRequestBody(idSinhVien, note)
            val result = apiService.editNote(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun deleteNote(id: Int): Flow<BaseResponse?> = flow {
        try {
            val idSinhVien = AppState.getInstance().getSinhVien().id.toString()
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

    override suspend fun getStudents(): Flow<ResponseSinhVien?> = flow {
        try {
            val selectedCourse = AppState.getInstance().getSelectedClass()
            val body = ModelHelper.buildStudentsRequestBody(selectedCourse!!)
            val result = apiService.getStudents(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun getPosts(): Flow<PostResponse?> = flow {
        try {
            val selectedCourse = AppState.getInstance().getSelectedClass()
            val body = ModelHelper.buildPostRequestBody(selectedCourse!!)
            val result = apiService.getPosts(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun deletePost(id: Int): Flow<BaseResponse?> = flow {
        try {
            val body = ModelHelper.buildDeleteByIdRequestBody(id)
            val result = apiService.deletePost(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun getMessages(): Flow<MessageResponse?> = flow {
        try {
            val postId = AppState.getInstance().selectedPost?.post?.id.guard { return@flow }
            val body = ModelHelper.buildGetMessagesRequestBody(postId)
            val result = apiService.getMessages(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun deleteMessage(id: Int): Flow<BaseResponse?> = flow {
        try {
            val body = ModelHelper.buildDeleteByIdRequestBody(id)
            val result = apiService.deleteMessage(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun editMessage(message: Message): Flow<BaseResponse?> = flow {
        try {
            val idSinhVien = AppState.getInstance().getSinhVien().id
            val body = ModelHelper.buildEditMessageRequestBody(idSinhVien, message)
            val result = apiService.editMessage(body)
            emit(result)
        } catch (exception: Exception) {
            emit(null)
            throw exception
        }
    }

    override suspend fun editPost(post: Post): Flow<BaseResponse?> = flow {
        try {
            val idSinhVien = AppState.getInstance().getSinhVien().id
            val body = ModelHelper.buildEditPostRequestBody(idSinhVien, post)
            val result = apiService.editPosts(body)
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