package com.example.note.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel: ViewModel() {
    private val _showLoading = MutableSharedFlow<Boolean>()
    val showLoading: SharedFlow<Boolean> = _showLoading

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    private val _toastRes = MutableSharedFlow<Int>()
    val toastRes: SharedFlow<Int> = _toastRes

    protected suspend fun showToast(message: String) {
        _toastMessage.emit(message)
    }

    protected suspend fun showToast(resId: Int) {
        _toastRes.emit(resId)
    }

    protected suspend fun showLoading() {
        _showLoading.emit(true)
    }
    protected suspend fun hideLoading() {
        _showLoading.emit(false)
    }
}