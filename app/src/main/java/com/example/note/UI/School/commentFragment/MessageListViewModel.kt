package com.example.note.UI.School.commentFragment

import com.example.note.Tools.log_helper.LogHelper
import com.example.note.UI.School.model.MessageResult
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
class MessageListViewModel @Inject constructor(): BaseViewModel() {
    val course: Course? = AppState.getInstance().getSelectedClass()

    private val _messageResults = MutableStateFlow<List<MessageResult>>(listOf())
    val messageResults: StateFlow<List<MessageResult>> = _messageResults

    private val _deleteResult = MutableSharedFlow<String>()
    val deleteResult: SharedFlow<String> = _deleteResult

    fun getMessageList() {
        launch {
            useCase.getMessages()
                .injectLoading()
                .catch { LogHelper.logDebug(this.javaClass, it.message) }
                .collect {
                    LogHelper.logDebug(this.javaClass, "list post to submit: ${it}")
                    _messageResults.value = it?.result ?: listOf()
                }
        }
    }

    fun deleteMessage(id: Int) {
        launch {
            useCase.deleteMessage(id)
                .injectLoading()
                .catch { LogHelper.logDebug(this.javaClass, it.message) }
                .collect {
                    getMessageList()
                    _deleteResult.emit(it?.message ?: "")
                }

        }
    }
}