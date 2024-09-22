package com.example.note.UI.home.home

import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseViewModel
import com.example.note.data.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(): BaseViewModel() {

    private val notes: MutableList<Note> = mutableListOf()
    private val _displayNotes = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val displayNotes: StateFlow<MutableList<Note>> = _displayNotes

    private val _deleteResponse = MutableSharedFlow<Boolean>()
    val deleteResponse: SharedFlow<Boolean> = _deleteResponse

    var keyWord: String = ""
    set(value) {
        field = value
        searchLocal()
    }

    fun deleteNote(noteId: Int) {
        launch {
            useCase.deleteNote(noteId)
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message.toString())
                }
                .collect { response ->
                    response?.let {
                        it.status?.let {
                            getNotes()
                        }
                        _deleteResponse.emit(it.status ?: false)
                    }
            }
        }
    }

    fun getNotes() {
        launch {
            useCase.getNoteById()
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message.toString())
                }
                .collect { response ->
                    response?.let {
                        notes.clear()
                        notes.addAll(it.notes)
                        _displayNotes.emit(notes)
                    }
                }
        }
    }


    private fun searchLocal() {
        launch {
            if (keyWord.isEmpty()) {
                _displayNotes.emit(notes)
                return@launch
            }
            else {
                val notesToSearch: MutableList<Note> = mutableListOf()
                notes.filter { it.tieuDe?.contains(keyWord) == true || it.noiDung?.contains(keyWord) == true}.let { list ->
                    notesToSearch.addAll(list)
                }
                _displayNotes.emit(notesToSearch)
            }
        }
    }

    fun shortByModify() {
        val data = _displayNotes.value
        data.sortWith{ note1, note2 ->
            java.lang.Long.compare(
                note1.ngayCapNhatDate?.time ?: 0,
                note2.ngayCapNhatDate?.time ?: 0
            )
        }
        launch { _displayNotes.emit(data) }
    }

    fun sortByCreate() {
        val data = _displayNotes.value
        data.sortWith{ note1, note2 ->
            java.lang.Long.compare(
                note1.ngayTaoDate?.time ?: 0,
                note2.ngayTaoDate?.time ?: 0
            )
        }
        launch { _displayNotes.emit(data) }
    }
}