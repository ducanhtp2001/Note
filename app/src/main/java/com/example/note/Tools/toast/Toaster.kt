package com.example.note.Tools.toast

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Toaster @Inject constructor(@ApplicationContext private val context: Context) {
    private var toast: Toast? = null

    fun display(message: String) {
        toast?.cancel()
        toast = makeText(context, message, LENGTH_LONG).also {
            it.setMargin(0f, -1f)
            it.setGravity(Gravity.FILL_HORIZONTAL, 0, 0)
            it.show()
        }
    }

    fun display(resourceId: Int) {
        val message = context.resources.getString(resourceId)
        toast?.cancel()
        toast = makeText(context, message, LENGTH_LONG).also {
            it.setMargin(0f, -1f)
            it.setGravity(Gravity.FILL_HORIZONTAL, 0, 0)
            //it.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 0)
            it.show()
        }
    }
}