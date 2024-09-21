package com.example.note.Data

import android.annotation.SuppressLint

class AppState {


    @SuppressLint("StaticFieldLeak")
    private object Holder {
        val INSTANCE = AppState()
    }

    companion object {
        @JvmStatic
        fun getInstance(): AppState {
            return Holder.INSTANCE
        }
    }

}