package com.example.note.UI.School.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.Tools.tryWithLog
import com.example.note.UI.School.model.MessageResult
import com.example.note.UI.School.model.PostResult
import com.example.note.data.AppState
import com.example.note.databinding.MessageItemBinding
import com.example.note.databinding.PostItemBinding

class MessageAdapter : ListAdapter<MessageResult, MessageAdapter.ViewHolder>(MessageDiff()) {

    interface MessageListener {
        fun deleteMessage(id: Int)
    }

    private var listener: MessageListener? = null

    fun setListener(listener: MessageListener) {
        this.listener = listener
    }

    inner class ViewHolder(
        val binding: MessageItemBinding,
        context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(result: MessageResult, position: Int) {

            val appUserId = AppState.getInstance().getSinhVien().id

            if (appUserId != result.info.id) {
                binding.btnDelete.visibility = View.GONE
            }

            binding.btnDelete.setOnClickListener {
                result.message.id?.let {
                    listener?.deleteMessage(it)
                }
            }

            binding.tvName.text = result.info.hoTen ?: ""
            binding.tvContent.text = result.message.noiDung ?: ""
            binding.tvDate.text = result.message.thoiGian ?: ""
            tryWithLog {
                result.message.imgOnBitmap?.let {
                    Glide.with(binding.root.context).load(it).into(binding.image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), context = parent.context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position), position)
    }
}

class MessageDiff : DiffUtil.ItemCallback<MessageResult>() {
    override fun areItemsTheSame(oldItem: MessageResult, newItem: MessageResult): Boolean {
        return oldItem.message.id == newItem.message.id
    }

    override fun areContentsTheSame(oldItem: MessageResult, newItem: MessageResult): Boolean {

        return oldItem == newItem
    }
}


