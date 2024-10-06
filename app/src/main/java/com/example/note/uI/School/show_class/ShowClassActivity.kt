package com.example.note.uI.School.show_class

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.note.Adapter.StudentAdapter
import com.example.note.base.BaseActivity
import com.example.note.data.model.SinhVien
import com.example.note.databinding.ClassmateLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowClassActivity : BaseActivity<ClassmateLayoutBinding>(), StudentAdapter.StudentListener {

    override val bindingInflater: (LayoutInflater) -> ClassmateLayoutBinding = { layoutInflater ->
        ClassmateLayoutBinding.inflate(layoutInflater)
    }

    override val viewModel: ShowClassViewModel by viewModels()

    private val studentAdapter = StudentAdapter()

    override fun viewDidLoad() {
        viewModel.getStudents()

        viewModel.course?.let {
            binding.classNameValues.text = it.tenLop
            binding.teacherValues.text = it.tenGiaoVien
            binding.totalCreditValues.text = it.soTinChi.toString()
        }
    }

    override fun initData() {

    }

    override fun initViews() {
        binding.itemsView.adapter = studentAdapter
        studentAdapter.setListener(this)
    }

    override fun setupViewEvents() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.students.bindTo {
            studentAdapter.submitList(it)
        }
    }



    override fun onStudentClick(sinhVien: SinhVien) {
        // Handle student click event here //TODO
    }
}