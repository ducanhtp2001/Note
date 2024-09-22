package com.example.note.data.model

data class ResponseNote(var status: Boolean = false, var notes: List<Note> = emptyList())
