package com.example.note.activities.home

import com.example.note.R

enum class DrawMenuType(val value: Int) {
    HOME(1),
    ACCOUNT(2),
    LOGOUT(4),
    TRASH(5);

    fun getDrawMenuResId() = when (this) {
        HOME -> R.id.nav_home
        ACCOUNT -> R.id.nav_account
        LOGOUT -> R.id.nav_logout
        TRASH -> R.id.nav_trash
    }

    companion object {
        fun getDrawMenuType(id: Int) = when(id) {
            R.id.nav_home -> HOME
            R.id.nav_account -> ACCOUNT
            R.id.nav_logout -> LOGOUT
            R.id.nav_trash -> TRASH
            else -> HOME
        }
    }
}

