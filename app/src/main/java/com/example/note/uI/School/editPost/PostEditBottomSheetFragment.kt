package com.example.note.uI.School.editPost

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
import com.example.note.Tools.tryWithLog
import com.example.note.uI.School.show_class.ShowClassActivity
import com.example.note.base.BaseBottomSheetDialogFragment
import com.example.note.base.provideViewModels
import com.example.note.data.AppState
import com.example.note.data.model.Post
import com.example.note.databinding.BottomSheetPostEditBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostEditBottomSheetFragment : BaseBottomSheetDialogFragment() {

    companion object {
        const val TAG = "PostEditBottomSheetFragment"
    }

    private lateinit var binding: BottomSheetPostEditBinding

    private val viewModel: PostEditViewModel by provideViewModels()

    private lateinit var behavior: BottomSheetBehavior<View>

    private var post: Post = Post()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPostEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        initData()
        setupViews(view)
    }

    private fun initData() {
        viewModel.post.value?.let {
            binding.topAppBar.title = getString(R.string.edit_post_title_new)
        } ?: run {
            val post = AppState.getInstance().selectedPost?.post ?: Post()
            this@PostEditBottomSheetFragment.post = post
            viewModel.updatePost(post)
            binding.topAppBar.title = getString(R.string.edit_post_title_edit)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }

    private fun setupViewEvents() {
        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
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

        binding.edNewPost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                post.noiDung = s.toString()
                viewModel.updatePost(post.copy())
            }
        })

        binding.btnImage.setOnClickListener {
            pickImageFromGallery()
        }

        binding.clearImage.setOnClickListener {
            binding.isHasImage = false
            post.coImg = 0
            post.img = null
            viewModel.updatePost(post.copy())
        }

        binding.btnPost.setOnClickListener {
            viewModel.submitPost()
        }
    }

    private fun bindViewModel() {
        viewModel.toastMessage bindTo toaster::display
        viewModel.toastRes bindTo toaster::display
        viewModel.postResult bindTo {
            dismiss()
        }
        viewModel.post.bindTo {
            if (it != post) {
                updatePost(it)
            }
        }

        viewModel.showLoading.bindTo {
            binding.isLoading = it
        }
    }

    private fun updatePost(p: Post?) {
        binding.edNewPost.setText(p?.noiDung ?: "")
        binding.isHasImage = p?.coImg == 1
        tryWithLog {
            GlobalScope.launch(Dispatchers.IO) {
                p?.imgOnBitmap?.let { bitmap ->
                    launch(Dispatchers.Main) {
                        Glide.with(requireContext()).load(bitmap).into(binding.image)
                    }
                }
            }
        }
    }

    private fun setupViews(view: View) {
        setupBottomSheetBehaviour(view)
        initView()
        setupViewEvents()
        bindViewModel()
    }

    private fun initView() {
        binding.isLoading = false
        viewModel.post.value?.let {
            updatePost(it)
        }
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
                binding.isHasImage = true
                val bitmap = ConvertImg.getBitmapFromUri(it, requireContext())
                post.coImg = 1
                post.img = ConvertImg.Image2String(bitmap)

                viewModel.updatePost(post.copy())
                tryWithLog { Glide.with(requireContext()).load(bitmap).into(binding.image) }
            }
        }
    }
}