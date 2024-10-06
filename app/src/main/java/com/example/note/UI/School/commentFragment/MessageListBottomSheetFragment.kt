package com.example.note.UI.School.commentFragment

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note.R
import com.example.note.Tools.dialogHelper.DialogHelper
import com.example.note.UI.School.adapter.MessageAdapter
import com.example.note.UI.School.show_class.ShowClassActivity
import com.example.note.base.BaseBottomSheetDialogFragment
import com.example.note.base.provideViewModels
import com.example.note.databinding.BottomSheetMessageListBinding
import com.example.note.databinding.BottomSheetPostListBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageListBottomSheetFragment : BaseBottomSheetDialogFragment(), MessageAdapter.MessageListener {

    companion object {
        const val TAG = "MessageListBottomSheetFragment"
    }

    private lateinit var binding: BottomSheetMessageListBinding

    private val viewModel: MessageListViewModel by provideViewModels()

    private lateinit var behavior: BottomSheetBehavior<View>

    private val messageAdapter = MessageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMessageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        initData()
        setupViews(view)
    }

    private fun initData() {
        viewModel.getMessageList()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }

    private fun setupViewEvents() {
        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getMessageList()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_goto_student_list -> {
                    startActivity(Intent(requireContext(), ShowClassActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun bindViewModel() {
        viewModel.messageResults.bindTo {
            messageAdapter.submitList(it)
        }

        viewModel.deleteResult.bindTo {
            toaster.display(it)
        }
    }

    private fun setupViews(view: View) {
        setupBottomSheetBehaviour(view)
        initView()
        setupViewEvents()
        bindViewModel()
    }

    private fun initView() {
        messageAdapter.setListener(this)
        binding.itemList.adapter = messageAdapter
    }

    private fun setupBottomSheetBehaviour(view: View) {
        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.isHideable = true
        behavior.isDraggable = false
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        // set min height to parent view
        binding.root.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
    }

    override fun deleteMessage(id: Int) {
        DialogHelper.showCustomConfirmDialog(requireContext(),
            getString(R.string.common_confirm),
            getString(R.string.common_confirm_delete),
            getString(R.string.common_ok),
            getString(R.string.common_cancel),
            positiveAction = {
                viewModel.deleteMessage(id)
            }
        )
    }
}