package com.example.note.data

import android.annotation.SuppressLint
import com.example.note.UI.School.model.PostResult
import com.example.note.data.model.Course
import com.example.note.data.model.Note
import com.example.note.data.model.SinhVien

class AppState {

    private var sinhVien: SinhVien = SinhVien()
    private var selectedNote: Note? = null
    private var selectedClass: Course? = null

    var selectedPost: PostResult? = null


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

    fun getSelectedNote(): Note? {
        return selectedNote
    }

    fun setSelectedNote(selectedNote: Note?) {
        this.selectedNote = selectedNote
    }

    fun getSelectedClass(): Course? {
        return selectedClass
    }

    fun setSelectedClass(selectedClass: Course?) {
        this.selectedClass = selectedClass
    }
}