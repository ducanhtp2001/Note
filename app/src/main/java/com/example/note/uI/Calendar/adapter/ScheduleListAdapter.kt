package com.example.note.uI.Calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note.data.model.Schedule
import com.example.note.databinding.ScheduleLayoutBinding

class ScheduleListAdapter : ListAdapter<Schedule, ScheduleListAdapter.ViewHolder>(ScheduleDiff()) {

    interface ScheduleListener {
        fun onScheduleClick(schedule: Schedule)
    }

    private var listener: ScheduleListener? = null

    fun setListener(listener: ScheduleListener) {
        this.listener = listener
    }

    inner class ViewHolder(
        val binding: ScheduleLayoutBinding,
        context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(schedule: Schedule, position: Int) {
            binding.tvScheTenMonHoc.text = schedule.tenMon
            binding.tvScheSoTinChi.text = "Số tín chỉ: ${schedule.soTinChi}"
            binding.tvScheMaMonHoc.text = schedule.maMon.toString()
            binding.tvScheLopTinChi.text = "L0${schedule.lopTinChi}"
            binding.tvSchePhongHoc.text = schedule.phongHoc
            binding.tvScheCaHoc.text = "Ca ${schedule.caHoc}"
            binding.tvScheNgayHoc.text = schedule.ngayHoc ?: ""
            binding.tvScheThoiGianHoc.text = schedule.thoiGianHoc

            binding.root.setOnClickListener {
                listener?.onScheduleClick(schedule)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ScheduleLayoutBinding.inflate(
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

class ScheduleDiff : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.ngayHoc == newItem.ngayHoc
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }
}