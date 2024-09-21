package com.example.note.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.Data.repository.repo.CommonRepository
import com.example.note.Data.useCase.CommonUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel: ViewModel() {

    @Inject
    lateinit var useCase: CommonUseCase

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

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        job: suspend () -> Unit
    ) =
        viewModelScope.launch(context) {
            job.invoke()
        }

    protected fun <T> Flow<T>.injectLoading(): Flow<T> = this
        .onStart { showLoading() }
        .onCompletion { hideLoading() }
}