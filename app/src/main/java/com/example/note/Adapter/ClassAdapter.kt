package com.example.note.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.data.model.Course
import com.example.note.databinding.ClassmateItemBinding

class ClassAdapter(): ListAdapter<Course, ClassAdapter.ViewHolder>(ClassDiff()) {

    interface ClassListener {
        fun onClassClick(course: Course, position: Int)
    }

    private var listener: ClassListener? = null

    fun setListener(listener: ClassListener) {
        this.listener = listener
    }

    inner class ViewHolder(
        val binding: ClassmateItemBinding,
        context: Context
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindItem(course: Course, position: Int) {
            LogHelper.logDebug(this.javaClass, "current list: $currentList")
            binding.className.text = course.tenMon
            binding.root.setOnClickListener {
                listener?.onClassClick(course, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ClassmateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), context = parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position), position)
    }
}

class ClassDiff: DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.maMon == newItem.maMon
    }

}


