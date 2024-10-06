package com.example.note.uI.School.commentFragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.note.R
import com.example.note.Tools.AnotherTools.ConvertImg
import com.example.note.Tools.PermissionUtils
import com.example.note.Tools.dialogHelper.DialogHelper
import com.example.note.Tools.tryWithLog
import com.example.note.uI.School.adapter.MessageAdapter
import com.example.note.uI.School.show_class.ShowClassActivity
import com.example.note.base.BaseBottomSheetDialogFragment
import com.example.note.base.provideViewModels
import com.example.note.data.model.Message
import com.example.note.databinding.BottomSheetMessageListBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessageListBottomSheetFragment : BaseBottomSheetDialogFragment(), MessageAdapter.MessageListener {

    companion object {
        const val TAG = "MessageListBottomSheetFragment"
    }

    private lateinit var binding: BottomSheetMessageListBinding

    private val viewModel: MessageListViewModel by provideViewModels()

    private lateinit var behavior: BottomSheetBehavior<View>

    private val messageAdapter = MessageAdapter()
    private var message = Message()

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
        viewModel.message.value?.let {
            message = it
        }
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

        binding.edNewMessage.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                message.noiDung = s.toString()
                viewModel.updateMessage(message.copy())
            }

        })

        binding.btnImage.setOnClickListener {
            pickImageFromGallery()
        }

        binding.btnSend.setOnClickListener {
            viewModel.submitMessage()
        }
    }

    private fun bindViewModel() {
        viewModel.toastRes bindTo toaster::display
        viewModel.toastMessage bindTo toaster::display

        viewModel.messageResults.bindTo {
            messageAdapter.submitList(it)
        }

        viewModel.deleteResult.bindTo {
            toaster.display(it)
        }

        viewModel.message.bindTo {
            if (message != it) {
                message = it ?: Message()
                binding.edNewMessage.setText(message.noiDung ?: "")
            }
        }

        viewModel.postResult.bindTo {
            clearData()
        }
    }

    private fun clearData() {
        viewModel.updateMessage(Message())
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

    private fun pickImageFromGallery() {

        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtils.askForPermission(
                requireActivity(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) {
                chooseImageGallery()
            }
        } else {
            PermissionUtils.askForPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                chooseImageGallery()
            }
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            val imageUri = intent?.data
            imageUri?.let {
                GlobalScope.launch(Dispatchers.IO) {
                    val bitmap = ConvertImg.getBitmapFromUri(it, requireContext())
                    message.coImg = 1
                    message.img = ConvertImg.Image2String(bitmap)

                    viewModel.updateMessage(message.copy())
                    launch(Dispatchers.Main) {
                        tryWithLog { Glide.with(requireContext()).load(bitmap).into(binding.btnImage) }
                    }
                }
            }
        }
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