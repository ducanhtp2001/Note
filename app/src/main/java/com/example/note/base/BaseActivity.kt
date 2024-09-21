package com.example.note.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.note.Tools.toast.Toaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity <VB: ViewBinding>: AppCompatActivity(), BaseActivityBehavior {

    protected abstract val viewModel: BaseViewModel

    protected abstract val bindingInflater: (LayoutInflater) -> VB

    private var _binding: ViewBinding? = null

    @Inject
    lateinit var toaster: Toaster

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingInflater.invoke(layoutInflater).apply { _binding = this }.root)

        initData()
        initViews()
        setupViewEvents()
        setupObservers()
        viewDidLoad()
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.toastRes bindTo toaster::display
        viewModel.toastMessage bindTo toaster::display
    }

    protected inline infix fun <T> Flow<T>.bindTo(crossinline action: (T) -> Unit) {
        with(lifecycle) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    collect { action(it) }
                }
            }
        }
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}