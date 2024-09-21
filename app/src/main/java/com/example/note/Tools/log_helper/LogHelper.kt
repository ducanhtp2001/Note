package com.example.note.Tools.log_helper

import android.util.Log

class LogHelper {
    companion object {
        private const val TAG = "DEBUG"
        private const val TAG_SEPARATOR = "     : "
        fun logDebug(msg: String?) {
            Log.d(TAG, msg.toString())
        }

        fun logDebug(className: Class<Any>, msg: String?) {
            val prefix = "DEBUG: " + className.simpleName
            Log.d(TAG, prefix + TAG_SEPARATOR + msg)
        }
    }
}