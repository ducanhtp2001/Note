package com.example.note.base

interface BaseFragmentBehavior {
    fun initData() {}
    fun initView() {}
    fun initViewModel() {}
    fun setupView() {}
    fun bindViewEvents() {}
    fun bindViewModel() {}
}
