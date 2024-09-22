package com.example.note.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import co.nimblehq.common.extensions.hideSoftKeyboard
import com.example.note.Tools.toast.Toaster
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import vn.gotrack.common.util.Toaster
import javax.inject.Inject

open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var toaster: Toaster

    open fun displayToast(message: String) {
        toaster.display(message)
    }

    open fun displayToast(stringId: Int) {
        toaster.display(stringId)
    }

    protected inline infix fun <T> Flow<T>.bindTo(crossinline action: (T) -> Unit) {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    collect { action(it) }
                }
            }
        }
    }
}