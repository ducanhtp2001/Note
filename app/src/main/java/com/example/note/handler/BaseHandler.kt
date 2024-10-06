package com.example.note.handler

import androidx.fragment.app.FragmentActivity
import com.example.note.Tools.AnotherTools.guard
import com.example.note.UI.School.commentFragment.MessageListBottomSheetFragment
import com.example.note.UI.School.editPost.PostEditBottomSheetFragment
import com.example.note.UI.School.postFragment.PostListBottomSheetFragment
import com.example.note.UI.home.add_note.EditNoteBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface BaseHandler {
    fun openEditBottomSheet(
        activity: FragmentActivity? = null,
        listener: EditNoteBottomSheetFragment.EditNoteListener? = null
    )

    fun openPostListBottomSheet(activity: FragmentActivity? = null)
    fun openPostEditBottomSheet(activity: FragmentActivity? = null)
    fun openMessageListBottomSheet(activity: FragmentActivity? = null)
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

    override fun openPostListBottomSheet(activity: FragmentActivity?) {
        val tagName = PostListBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as PostListBottomSheetFragment?).guard {
                val dialog = PostListBottomSheetFragment()
                dialog.show(transaction, tagName)
                return
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
    }

    override fun openPostEditBottomSheet(activity: FragmentActivity?) {
        val tagName = PostEditBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as PostEditBottomSheetFragment?).guard {
                val dialog = PostEditBottomSheetFragment()
                dialog.show(transaction, tagName)
                return
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
    }

    override fun openMessageListBottomSheet(activity: FragmentActivity?) {
        val tagName = MessageListBottomSheetFragment.TAG
        val transaction = activity?.supportFragmentManager?.beginTransaction().guard { return }
        val previous =
            (activity?.supportFragmentManager
                ?.findFragmentByTag(tagName) as MessageListBottomSheetFragment?).guard {
                val dialog = MessageListBottomSheetFragment()
                dialog.show(transaction, tagName)
                return
            }
        transaction.remove(previous)
        previous.show(transaction, tagName)
    }

}