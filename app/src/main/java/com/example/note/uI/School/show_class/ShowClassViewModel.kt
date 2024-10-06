package com.example.note.uI.School.show_class

import com.example.note.base.BaseViewModel
import com.example.note.data.AppState
import com.example.note.data.model.SinhVien
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class ShowClassViewModel @Inject constructor(): BaseViewModel() {
    private val _students = MutableStateFlow<MutableList<SinhVien>?>(mutableListOf())
    val students: StateFlow<List<SinhVien>?> = _students

    val course = AppState.getInstance().getSelectedClass()

    fun getStudents() {
        launch {
            useCase.getStudents()
                .injectLoading()
                .catch {  }
                .collect { response ->
                    response?.let {
                        _students.emit(it.sinhVien?.toMutableList())
                    }
                }
        }
    }
}