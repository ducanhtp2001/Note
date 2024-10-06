package com.example.note.uI.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.databinding.LoadingLayoutBoxBinding

class LoadingIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: LoadingLayoutBoxBinding = LoadingLayoutBoxBinding.inflate(
        LayoutInflater.from(context), this
    )


    init {
        setupUI()
    }

    private fun setupUI() {
        binding.loadingContentView.setOnClickListener {
            LogHelper.logDebug(this.javaClass, "loading background Clicked.")
        }
    }

    fun updateLabel(label: String) {
        binding.txtLabel.text = label
    }
}