package com.example.note.data

import android.annotation.SuppressLint
import com.example.note.data.model.Course

class AppState {

    private var idSinhVien: String? = null
    private var course: Course? = null


    @SuppressLint("StaticFieldLeak")
    private object Holder {
        val INSTANCE = AppState()
    }

    companion object {
        @JvmStatic
        fun getInstance(): AppState {
            return Holder.INSTANCE
        }
    }

    fun getIdSinhVien(): String? {
        return idSinhVien;
    }

    fun setIdSinhVien(idSinhVien: String) {
        this.idSinhVien = idSinhVien;
    }

    fun setCourse(course: Course) {
        this.course = course;
    }

    fun getCourse(): Course? {
        return course;
    }


}