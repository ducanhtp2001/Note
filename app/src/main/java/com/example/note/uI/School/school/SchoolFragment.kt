package com.example.note.uI.School.school

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.note.Adapter.ClassAdapter
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseFragment
import com.example.note.base.provideViewModels
import com.example.note.data.AppState
import com.example.note.data.model.Course
import com.example.note.databinding.FragmentSchoolBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SchoolFragment : BaseFragment<FragmentSchoolBinding>(), ClassAdapter.ClassListener {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSchoolBinding
        get() = { inflater, container, attachToParent ->
            FragmentSchoolBinding.inflate(inflater, container, attachToParent)
        }

    private val viewModel: SchoolViewModel by provideViewModels()

    private val adapter = ClassAdapter()

    override fun setupView() {
        super.setupView()
        binding.listViewSchool.adapter = adapter
        adapter.setListener(this)

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun viewDidLoad() {
        viewModel.getClasses()
    }

    override fun bindViewModel() {
        super.bindViewModel()

        viewModel.classes.bindTo {
            LogHelper.logDebug(this.javaClass, it.toString())
            adapter.submitList(it)
        }
    }

    override fun onClassClick(course: Course, position: Int) {
        AppState.getInstance().setSelectedClass(course)
        openPostListBottomSheet(requireActivity())
    }

}