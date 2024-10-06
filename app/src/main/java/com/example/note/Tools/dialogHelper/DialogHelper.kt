package com.example.note.Tools.dialogHelper

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.note.databinding.DialogCustomConfirmBinding

class DialogHelper {

    companion object {
        fun showCustomConfirmDialog(
            context: Context,
            title: String,
            message: String,
            titlePositive: String,
            titleNegative: String,
            positiveAction: () -> Unit,
            negativeAction: (() -> Unit)? = null,
            cancelable: Boolean = true
        ) {
            val dialog = Dialog(context)
            val binding = DialogCustomConfirmBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            dialog.setCancelable(cancelable)
            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            binding.apply {
                tvTitle.text = title
                tvMessage.text = message
                btnNegative.text = titleNegative
                btnPositive.text = titlePositive
                btnNegative.setOnClickListener {
                    negativeAction?.let { action ->
                        action.invoke()
                        dialog.cancel()
                    } ?: run {
                        dialog.cancel()
                    }
                }
                btnPositive.setOnClickListener {
                    positiveAction()
                    dialog.cancel()
                }
            }

            dialog.show()
        }
    }


}