package com.example.note.handler

import androidx.fragment.app.FragmentActivity
import com.example.note.Tools.AnotherTools.guard
import com.example.note.uI.School.commentFragment.MessageListBottomSheetFragment
import com.example.note.uI.School.editPost.PostEditBottomSheetFragment
import com.example.note.uI.School.postFragment.PostListBottomSheetFragment
import com.example.note.uI.home.add_note.EditNoteBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface BaseHandler {
    fun openEditBottomSheet(
        activity: FragmentActivity? = null,
        listener: EditNoteBottomSheetFragment.EditNoteListener? = null
    ): BottomSheetDialogFragment?

    fun openPostListBottomSheet(activity: FragmentActivity? = null): BottomSheetDialogFragment?
    fun openPostEditBottomSheet(activity: FragmentActivity? = null): BottomSheetDialogFragment?
    fun openMessageListBottomSheet(activity: FragmentActivity? = null): BottomSheetDialogFragment?
}

class BaseHandlerImpl : BaseHandler {
    override fun openEditBottomSheet(
        activity: FragmentActivity?,
        listener: EditNoteBottomSheetFragment.EditNoteListener?
    ): BottomSheetDialogFragment? {
        val tagName = EditNoteBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return null }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as BottomSheetDialogFragment?).guard {
                val dialog = EditNoteBottomSheetFragment(listener)
                dialog.show(transaction, tagName)
                return dialog
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
        return previous
    }

    override fun openPostListBottomSheet(activity: FragmentActivity?): BottomSheetDialogFragment? {
        val tagName = PostListBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return null }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as PostListBottomSheetFragment?).guard {
                val dialog = PostListBottomSheetFragment()
                dialog.show(transaction, tagName)
                return dialog
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
        return previous
    }

    override fun openPostEditBottomSheet(activity: FragmentActivity?): BottomSheetDialogFragment? {
        val tagName = PostEditBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return null }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as PostEditBottomSheetFragment?).guard {
                val dialog = PostEditBottomSheetFragment()
                dialog.show(transaction, tagName)
                return dialog
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
        return previous
    }

    override fun openMessageListBottomSheet(activity: FragmentActivity?): BottomSheetDialogFragment? {
        val tagName = MessageListBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return null }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as MessageListBottomSheetFragment?).guard {
                val dialog = MessageListBottomSheetFragment()
                dialog.show(transaction, tagName)
                return dialog
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
        return previous
    }

}