package com.example.note.UI.School.school

import com.example.note.R
import com.example.note.base.BaseViewModel
import com.example.note.data.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class SchoolViewModel @Inject constructor(): BaseViewModel() {
    private val _classes = MutableStateFlow<MutableList<Course>>(mutableListOf())
    val classes: StateFlow<List<Course>> = _classes

    fun getClasses() {
        launch {
            useCase.getClassById()
                .injectLoading()
                .catch {  }
                .collect { response ->
                    if (response?.status == true) {
                        _classes.emit(response.courses.toMutableList())
                    } else {
                        showToast(R.string.common_no_data)
                    }
                }
        }
    }
}