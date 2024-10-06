package com.example.note.UI.School.model

import com.example.note.data.model.Message
import com.example.note.data.model.Post
import com.example.note.data.model.SinhVien

data class MessageResult(
    var message: Message,
    var info: SinhVien
)

data class MessageResponse (
    var status: Boolean? = false,
    var result: List<MessageResult>? = emptyList()
)