package com.example.note.activities.register

import com.example.note.R
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseViewModel
import com.example.note.data.model.SinhVien
import com.example.note.data.model.TaiKhoan
import com.example.note.data.response.BaseResponse
import com.example.note.data.response.CheckRegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(): BaseViewModel() {

    private val _sinhVien = MutableStateFlow(SinhVien())
    val sinhVien: StateFlow<SinhVien> = _sinhVien

    private val _registerSuccess = MutableStateFlow<BaseResponse?>(null)
    val registerSuccess: StateFlow<BaseResponse?> = _registerSuccess

    val password = ""
    val passwordConfirm = ""

    fun register(idSinhVien: Int, account: String, password: String) {
        launch {
            val taiKhoan = TaiKhoan(idSinhVien, account, password)
            useCase
                .register(taiKhoan = taiKhoan)
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message)
                    _toastRes.emit(R.string.common_err)
                }
                .collect { response ->
                    response?.let {
                        _registerSuccess.emit(it)
                    } ?: run {
                        _toastRes.emit(R.string.common_err)
                    }
                }
        }
    }

    fun checkRegister(idSinhVien: String, callback: ((CheckRegisterResponse?) -> Unit)? = null) {
        launch {
            useCase
                .checkRegister(idSinhVien)
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message)
                    _toastRes.emit(R.string.common_err)
                }
                .collect { response ->
                    response?.let {
                        if (it.status == true) {
                            val newSinhVien = SinhVien()
                            newSinhVien.idStr = idSinhVien
                            newSinhVien.hoTen = it.tenSinhVien ?: ""
                            _sinhVien.emit(newSinhVien)
                            callback?.invoke(it)
                        } else {
                            _toastRes.emit(R.string.err_student_id_not_found)
                        }
                    }
                }
        }
    }
}