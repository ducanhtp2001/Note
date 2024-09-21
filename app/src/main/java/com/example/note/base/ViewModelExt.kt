package com.example.note.base

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

@MainThread
inline fun <reified VM : ViewModel> Fragment.provideActivityViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = OverridableLazy(activityViewModels(null, factoryProducer))

@MainThread
inline fun <reified VM : ViewModel> Fragment.provideViewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = OverridableLazy(viewModels(ownerProducer, null, factoryProducer))

class OverridableLazy<T>(private var implementation: Lazy<T>) : Lazy<T> {

    override val value
        get() = implementation.value

    override fun isInitialized() = implementation.isInitialized()
}

/**
 * Reference: https://proandroiddev.com/testing-the-untestable-the-case-of-the-viewmodel-delegate-975c09160993
 */