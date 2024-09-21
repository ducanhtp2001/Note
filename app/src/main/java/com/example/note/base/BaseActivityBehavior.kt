package com.example.note.base

interface BaseActivityBehavior {
    fun initViews() {}
    fun initData() {}
    fun setupViewEvents() {}
    fun setupObservers() {}
    fun viewDidLoad()
}