package com.example.note.activities.register

import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseViewModel
import kotlinx.coroutines.flow.catch

class RegisterViewModel: BaseViewModel() {
    fun checkRegister(idSinhVien: String) {
        launch {
            useCase
                .checkRegister(idSinhVien)
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message)
                }
                .collect { response ->
                    response?.let {
                        LogHelper.logDebug(this.javaClass, "status = ${it.status}, tenSinhVien = ${it.tenSinhVien}")
                    }
                }
        }
    }
}