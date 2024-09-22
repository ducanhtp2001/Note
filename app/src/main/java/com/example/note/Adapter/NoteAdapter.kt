package com.example.note.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note.R
import com.example.note.Tools.date_time.DateTimeHepler
import com.example.note.data.model.Note
import com.example.note.databinding.NoteLayoutBinding

class NoteAdapter : ListAdapter<Note, NoteAdapter.ViewHolder>(NoteDiff()){

    interface NoteAdapterListener {
        fun onNoteClick(note: Note)
    }

    private var listener: NoteAdapterListener? = null

    fun setListener(listener: NoteAdapterListener) {
        this.listener = listener
    }

    inner class ViewHolder(private val binding: NoteLayoutBinding, private val context: Context): RecyclerView.ViewHolder(binding.root) {

        fun bindItem(note: Note) {
            binding.noteTitle.text = note.tieuDe
            try {
                binding.editTime.text = DateTimeHepler.RESPONSE_SDF.format(note.ngayCapNhat)
            } catch (_: Exception) {}
            binding.noteBelong.text = note.noiDungCua

            binding.root.setOnClickListener {
                listener?.onNoteClick(note)
            }

            val animation = AnimationUtils.loadAnimation(context, R.anim.listview_anim)
            binding.root.startAnimation(animation)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false), context = parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
}

class NoteDiff: DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

}
