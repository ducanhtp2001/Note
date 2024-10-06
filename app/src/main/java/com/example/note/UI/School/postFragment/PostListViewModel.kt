package com.example.note.UI.School.postFragment

import com.example.note.Tools.log_helper.LogHelper
import com.example.note.UI.School.model.PostResult
import com.example.note.base.BaseViewModel
import com.example.note.data.AppState
import com.example.note.data.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(): BaseViewModel() {
    val course: Course? = AppState.getInstance().getSelectedClass()

    private val _postResults = MutableStateFlow<List<PostResult>>(listOf())
    val postResults: StateFlow<List<PostResult>> = _postResults

    private val _deleteResult = MutableSharedFlow<String>()
    val deleteResult: SharedFlow<String> = _deleteResult

    fun getPostList() {
        launch {
            useCase.getPosts()
                .injectLoading()
                .catch { LogHelper.logDebug(this.javaClass, it.message) }
                .collect {
                    LogHelper.logDebug(this.javaClass, "list post to submit: ${it}")
                    _postResults.value = it?.result ?: listOf()
                }
        }
    }

    fun deletePost(id: Int) {
        launch {
            useCase.deletePost(id)
                .injectLoading()
                .catch { LogHelper.logDebug(this.javaClass, it.message) }
                .collect {
                    getPostList()
                    _deleteResult.emit(it?.message ?: "")
                }

        }
    }
}