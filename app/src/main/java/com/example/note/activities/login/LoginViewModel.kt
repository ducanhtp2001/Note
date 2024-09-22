package com.example.note.activities.login

import com.example.note.R
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseViewModel
import com.example.note.data.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): BaseViewModel() {

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess

    fun login(account: String, password: String) {
        launch {
            useCase.login(account, password)
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message.toString())
                }
                .collect { response ->
                    response?.let {
                        it.message?.let { msg ->
                            _toastMessage.emit(msg)
                        }
                        if (response.status == true) {
                            AppState.getInstance().setIdSinhVien(account)
                            _loginSuccess.emit(true)
                        }
                    } ?: run {
                        _toastRes.emit(R.string.common_err)
                    }
                }
        }
    }
}