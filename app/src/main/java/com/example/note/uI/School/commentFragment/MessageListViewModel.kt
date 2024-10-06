package com.example.note.uI.School.commentFragment

import com.example.note.R
import com.example.note.Tools.AnotherTools.guard
import com.example.note.Tools.date_time.DateTimeHepler
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.uI.School.model.MessageResult
import com.example.note.base.BaseViewModel
import com.example.note.data.AppState
import com.example.note.data.model.Course
import com.example.note.data.model.Message
import com.example.note.data.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class MessageListViewModel @Inject constructor(): BaseViewModel() {
    val post: Post? = AppState.getInstance().selectedPost?.post

    private val _messageResults = MutableStateFlow<List<MessageResult>>(listOf())
    val messageResults: StateFlow<List<MessageResult>> = _messageResults

    private val _deleteResult = MutableSharedFlow<String>()
    val deleteResult: SharedFlow<String> = _deleteResult

    private val _message = MutableStateFlow<Message?>(null)
    val message: StateFlow<Message?> = _message

    val postResult = MutableSharedFlow<Boolean>()

    fun updateMessage(post: Message) {
        if (post != _message.value) {
            _message.value = post
        }
    }

    fun submitMessage() {
        launch {
            _message.value?.let { it ->
                if (it.noiDung?.isEmpty() == true) {
                    _toastRes.emit(R.string.edit_post_err_fill_content)
                } else {
                    val idPost = post?.id.guard {
                        LogHelper.logDebug(this.javaClass, "post is null")
                        _toastRes.emit(R.string.common_err)
                        return@launch
                    }
                    val data = it.copy(thoiGian = DateTimeHepler.getCurrentTime(), idPost = idPost)
                    useCase.editMessage(data)
                        .injectLoading()
                        .catch { e ->
                            LogHelper.logDebug(this.javaClass, e.message)
                        }
                        .collect { response ->
                            getMessageList()
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