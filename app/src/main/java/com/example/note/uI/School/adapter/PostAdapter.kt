package com.example.note.uI.School.adapter

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
import com.example.note.uI.School.model.PostResult
import com.example.note.data.AppState
import com.example.note.databinding.PostItemBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostAdapter : ListAdapter<PostResult, PostAdapter.ViewHolder>(PostDiff()) {

    interface PostListener {
        fun showPostComment(post: PostResult, position: Int)
        fun showPostClick(post: PostResult, position: Int)
        fun deletePost(id: Int)
    }

    private var listener: PostListener? = null

    fun setListener(listener: PostListener) {
        this.listener = listener
    }

    inner class ViewHolder(
        val binding: PostItemBinding,
        context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        @OptIn(DelicateCoroutinesApi::class)
        fun bindItem(result: PostResult, position: Int) {

            val appUserId = AppState.getInstance().getSinhVien().id

            if (appUserId != result.info.id) {
                binding.btnDelete.visibility = View.GONE
            }

            binding.btnDelete.setOnClickListener {
                result.post.id?.let {
                    listener?.deletePost(it)
                }
            }

            binding.tvName.text = result.info.hoTen ?: ""
            binding.tvContent.text = result.post.noiDung ?: ""
            LogHelper.logDebug(this.javaClass, "post: ${result.post}")
            binding.tvDate.text = result.post.thoiGian ?: ""
//            Glide.with(binding.root.context).load(result.info.avatar).into(binding.avatar)
            tryWithLog {
                GlobalScope.launch(Dispatchers.Main) {
                    if (result.post.coImg == 1) {
                        binding.image.visibility = View.VISIBLE
                        GlobalScope.launch(Dispatchers.Main) {
                            result.post.imgOnBitmap?.let {
                                Glide.with(binding.root.context)
                                    .load(it)
                                    .override(100)
                                    .centerCrop()
                                    .into(binding.image)
                            }
                        }
                    } else {
                        binding.image.visibility = View.GONE
                    }
                }

            }


            binding.root.setOnClickListener {
                AppState.getInstance().selectedPost = result
                listener?.showPostClick(result, position)
            }

            binding.comment.setOnClickListener {
                AppState.getInstance().selectedPost = result
                listener?.showPostComment(result, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PostItemBinding.inflate(
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

class PostDiff : DiffUtil.ItemCallback<PostResult>() {
    override fun areItemsTheSame(oldItem: PostResult, newItem: PostResult): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PostResult, newItem: PostResult): Boolean {
        return oldItem.post.id == newItem.post.id
    }
}


