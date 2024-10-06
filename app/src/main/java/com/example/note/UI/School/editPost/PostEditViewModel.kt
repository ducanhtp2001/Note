package com.example.note.UI.School.editPost

import com.example.note.R
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseViewModel
import com.example.note.data.AppState
import com.example.note.data.model.Course
import com.example.note.data.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(): BaseViewModel() {
    val course: Course? = AppState.getInstance().getSelectedClass()

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post

    val postResult = MutableSharedFlow<Boolean>()

    fun updatePost(post: Post) {
        if (post != _post.value) {
            _post.value = post
        }
    }

    fun submitPost() {
        launch {
            _post.value?.let {
                useCase.editPost(it)
                    .injectLoading()
                    .catch { LogHelper.logDebug(this.javaClass, it.message) }
                    .collect { response ->
                        response?.message?.let { message ->
                            _toastMessage.emit(message)
                        } ?: run {
                            _toastRes.emit(R.string.common_err)
                        }
                        postResult.emit(true)
                    }

            }
        }
    }
}