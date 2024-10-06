package com.example.note.uI.Calendar

import com.example.note.R
import com.example.note.Tools.AnotherTools.guard
import com.example.note.Tools.date_time.DateTimeHepler
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseViewModel
import com.example.note.data.AppState
import com.example.note.data.model.Schedule
import com.example.note.uI.Calendar.CalendarToolsModel.MonHoc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(): BaseViewModel() {

    private val originalSchedules = mutableListOf<Schedule>()

    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedule: StateFlow<List<Schedule>?> = _schedules

    val postResult = MutableSharedFlow<Boolean>()

    fun getCalendarOnDate(date: Date) {
        launch {
            val schedules = originalSchedules.filter {
                it.ngayHoc?.let { dateStr ->
                    DateTimeHepler.reportTimeToDate(dateStr) == date
                } ?: false
            }
            _schedules.emit(schedules)
        }
    }

    fun getCalendar() {
        launch {
            useCase.getCalendar()
                .injectLoading()
                .catch { LogHelper.logDebug(this.javaClass, it.message) }

                .collect { response ->
                    response?.let {
                        originalSchedules.clear()
                        originalSchedules.addAll(it.schedules ?: emptyList())
                    } ?: run {
                        _toastRes.emit(R.string.common_err)
                    }
                }
        }
    }

    fun submitSchedule(schedule: List<MonHoc>) {
        launch {
            useCase
                .editCalendar(schedule)
                .injectLoading()
                .catch { LogHelper.logDebug(this.javaClass, it.message) }
                .collect { response ->
                    getCalendar()
                    response?.let {
                        _toastMessage.emit(it.message ?: "")
                    } ?: run {
                        _toastRes.emit(R.string.common_err)
                    }
                }
        }
    }

}