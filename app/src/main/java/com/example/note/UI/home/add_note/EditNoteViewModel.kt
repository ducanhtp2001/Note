package com.example.note.UI.home.add_note

import com.example.note.Tools.date_time.DateTimeHepler
import com.example.note.base.BaseViewModel
import com.example.note.data.AppState
import com.example.note.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor() : BaseViewModel() {
    var note: Note = Note()

    fun getNote(): Note {
        return note
    }
}