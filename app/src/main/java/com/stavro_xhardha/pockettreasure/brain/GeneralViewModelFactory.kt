package com.stavro_xhardha.pockettreasure.brain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class GeneralViewModelFactory @Inject constructor(private val viewModelCreators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = viewModelCreators[modelClass] ?: viewModelCreators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key) //which means: does it extend a ViewModel or what?
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}