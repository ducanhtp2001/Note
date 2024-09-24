package com.example.note.data.model

data class ResponseSinhVien(
    val status: Boolean? = false,
    val sinhVien: List<SinhVien>? = emptyList()
)
