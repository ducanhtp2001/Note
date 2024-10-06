package com.example.note.uI.School.model

import com.example.note.data.model.Post
import com.example.note.data.model.SinhVien

data class PostResult(
    var post: Post,
    var info: SinhVien
)

data class PostResponse (
    var status: Boolean? = false,
    var result: List<PostResult>? = emptyList()
)