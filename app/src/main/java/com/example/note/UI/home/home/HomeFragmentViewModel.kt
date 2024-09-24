package com.example.note.UI.home.home

import com.example.note.R
import com.example.note.Tools.date_time.DateTimeHepler
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

    fun editNote(note: Note) {
        launch {
            useCase.editNote(note)
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message.toString())
                }
                .collect { response ->
                    response?.status?.let {
                        if (it) {
                            getNotes()
                        }
                    }
                    response?.message?.let {
                        showToast(it)
                    }
                }
        }
    }

    fun deleteNote(noteId: Int) {
        launch {
            useCase.deleteNote(noteId)
                .injectLoading()
                .catch {
                    LogHelper.logDebug(this.javaClass, it.message.toString())
                }
                .collect { response ->
                    response?.let { it ->
                        it.status?.let {
                            val notes = _displayNotes.value
                            notes.firstOrNull { it.id == noteId }?.let { noteToDelete ->
                                notes.remove(noteToDelete)
                            }
                            _displayNotes.emit(notes)
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
                        _displayNotes.emit(it.notes.toMutableList())
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
                DateTimeHepler.reportTimeToDate(note1.ngayCapNhat)?.time ?: 0,
                DateTimeHepler.reportTimeToDate(note2.ngayCapNhat)?.time ?: 0
            )
        }
        launch { _displayNotes.emit(data) }
    }

    fun sortByCreate() {
        val data = _displayNotes.value
        data.sortWith{ note1, note2 ->
            java.lang.Long.compare(
                DateTimeHepler.reportTimeToDate(note1.ngayTao)?.time ?: 0,
                DateTimeHepler.reportTimeToDate(note2.ngayTao)?.time ?: 0
            )
        }
        launch { _displayNotes.emit(data) }
    }
}