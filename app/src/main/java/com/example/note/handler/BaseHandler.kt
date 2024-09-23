package com.example.note.handler

import androidx.fragment.app.FragmentActivity
import com.example.note.Tools.AnotherTools.guard
import com.example.note.UI.home.add_note.EditNoteBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface BaseHandler {
    fun openEditBottomSheet(
        activity: FragmentActivity? = null,
        listener: EditNoteBottomSheetFragment.EditNoteListener? = null
    )
}

class BaseHandlerImpl : BaseHandler {
    override fun openEditBottomSheet(
        activity: FragmentActivity?,
        listener: EditNoteBottomSheetFragment.EditNoteListener?
    ) {
        val tagName = EditNoteBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as BottomSheetDialogFragment?).guard {
                val dialog = EditNoteBottomSheetFragment(listener)
                dialog.show(transaction, tagName)
                return
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
    }

}