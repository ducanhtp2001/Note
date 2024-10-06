package com.example.note.uI.School.editPost

import com.example.note.App
import com.example.note.R
import com.example.note.Tools.AnotherTools.guard
import com.example.note.Tools.date_time.DateTimeHepler
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
                if (it.noiDung?.isEmpty() == true) {
                    _toastRes.emit(R.string.edit_post_err_fill_content)
                } else {
                    val course = AppState.getInstance().getSelectedClass()
                    val maMon = course?.maMon.guard {
                        LogHelper.logDebug(this.javaClass, "course is null")
                        _toastRes.emit(R.string.common_err)
                        return@launch
                    }
                    val lopTinChi = course?.lopTinChi.guard {
                        LogHelper.logDebug(this.javaClass, "course is null")
                        _toastRes.emit(R.string.common_err)
                        return@launch
                    }
                    val data = it.copy(thoiGian = DateTimeHepler.getCurrentTime(), maMon = maMon, lopTinChi = lopTinChi)
                    useCase.editPost(data)
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
}