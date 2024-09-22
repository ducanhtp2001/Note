package com.example.note.UI.home.home

import com.example.note.base.BaseViewModel
import com.example.note.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(): BaseViewModel() {

    private val notes: MutableList<Note> = mutableListOf()
    private val _displayNotes = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val displayNotes: StateFlow<MutableList<Note>> = _displayNotes

    private var keyWord: String = ""
    set(value) {
        field = value
        searchLocal()
    }

    private fun searchLocal() {
        val notesToSearch: MutableList<Note> = mutableListOf()
        notes.filter { it.tieuDe.contains(keyWord) || it.noiDung.contains(keyWord) }.let { list ->
            notesToSearch.addAll(list)
        }
    }
}