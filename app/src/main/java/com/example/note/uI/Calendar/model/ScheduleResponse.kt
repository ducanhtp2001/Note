package com.example.note.uI.Calendar.model

import com.example.note.data.model.Schedule

data class ScheduleResponse(
    val status: Boolean? = false,
    val schedules: List<Schedule>?
)