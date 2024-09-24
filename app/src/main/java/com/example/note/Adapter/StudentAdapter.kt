package com.example.note.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note.data.model.SinhVien
import com.example.note.databinding.StudentItemBinding

class StudentAdapter : ListAdapter<SinhVien, StudentAdapter.ViewHolder>(SinhVienDiff()) {

    interface StudentListener {
        fun onStudentClick(sinhVien: SinhVien)
    }

    private var listener: StudentListener? = null

    fun setListener(listener: StudentListener) {
        this.listener = listener
    }

    inner class ViewHolder(
        val binding: StudentItemBinding,
        val context: Context
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindItem(sinhVien: SinhVien) {
            binding.studentName.text = sinhVien.hoTen
            binding.studentId.text = sinhVien.maSinhVien

            binding.root.setOnClickListener {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(StudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }
}

class SinhVienDiff: DiffUtil.ItemCallback<SinhVien>() {
    override fun areItemsTheSame(oldItem: SinhVien, newItem: SinhVien): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SinhVien, newItem: SinhVien): Boolean {
        return oldItem == newItem
    }

}
