package com.example.note.UI.home.add_note

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note.R
import com.example.note.Tools.AnotherTools.guard
import com.example.note.Tools.addTextWatcher
import com.example.note.Tools.date_time.DateTimeHepler
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseBottomSheetDialogFragment
import com.example.note.base.provideViewModels
import com.example.note.data.AppState
import com.example.note.data.model.Note
import com.example.note.databinding.BottomSheetEditNoteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteBottomSheetFragment(
    private var listener: EditNoteListener? = null,
) : BaseBottomSheetDialogFragment() {

    companion object {
        const val TAG = "EditNoteBottomSheetFragment"
    }

    interface EditNoteListener {
        fun onSave(note: Note)
    }

    private lateinit var binding: BottomSheetEditNoteBinding

    private val viewModel: EditNoteViewModel by provideViewModels()

    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listener.guard {
            dismiss()
            return null
        }
        binding = BottomSheetEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        initData()
        setupViews(view)
    }

    private fun initData() {
        AppState.getInstance().getSelectedNote()?.let {
            viewModel.note = it
        } ?: run {
            Note().apply {
                val time = DateTimeHepler.RESPONSE_SDF.format(System.currentTimeMillis())
                ngayCapNhat = time
                ngayTao = time
            }.let {
                viewModel.note = it
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }

    private fun setupViewEvents() {

        binding.backButton.setOnClickListener {
            dismiss()
        }

        binding.addNoteTitle.addTextWatcher {
            viewModel.getNoteEdited().tieuDe = it.toString()
        }

        binding.mainContain.addNoteContent.addTextWatcher {
            viewModel.getNoteEdited().noiDung = it.toString()
        }

        binding.fabSaveNote.setOnClickListener {
            LogHelper.logDebug("save note: ${viewModel.getNoteEdited()}")
            viewModel.getNoteEdited().let {
                listener?.onSave(it)
                dismiss()
            }
        }
    }

    private fun bindViewModel() {
        viewModel.getNoteEdited().noiDungCua = context?.getString(R.string.note_belong) ?: ""
    }

    private fun setupViews(view: View) {
        setupBottomSheetBehaviour(view)
        initView()
        setupViewEvents()
        bindViewModel()
    }

    private fun initView() {
        binding.addNoteTitle.setText(viewModel.getNoteEdited().tieuDe)
        binding.mainContain.addNoteContent.setText(viewModel.getNoteEdited().noiDung)
        binding.mainContain.lastEdit.text = viewModel.getNoteEdited().ngayCapNhat
    }

    private fun setupBottomSheetBehaviour(view: View) {
        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.isHideable = true
        behavior.isDraggable = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        // set min height to parent view
        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
    }
}