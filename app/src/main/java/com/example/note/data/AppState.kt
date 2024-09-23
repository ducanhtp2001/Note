package com.example.note.data

import android.annotation.SuppressLint
import com.example.note.data.model.Course
import com.example.note.data.model.Note
import com.example.note.data.model.SinhVien

class AppState {

    private var sinhVien: SinhVien = SinhVien()
    private var course: Course = Course()
    private var selectedNote: Note? = null


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

    fun clearData() {
        sinhVien = SinhVien()
        course = Course()
        selectedNote = null
    }

    fun getSinhVien(): SinhVien {
        return sinhVien;
    }

    fun setSinhVien(sinhVien: SinhVien) {
        this.sinhVien = sinhVien;
    }

    fun getIdSinhVien(): String {
        return sinhVien.idStr;
    }

    fun setIdSinhVien(idSinhVien: String) {
        this.sinhVien.idStr = idSinhVien;
    }

    fun setCourse(course: Course) {
        this.course = course;
    }

    fun getCourse(): Course {
        return course;
    }

    fun getSelectedNote(): Note? {
        return selectedNote
    }

    fun setSelectedNote(selectedNote: Note?) {
        this.selectedNote = selectedNote
    }
}