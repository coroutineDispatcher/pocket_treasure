package com.stavro_xhardha.core_module.brain

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.stavro_xhardha.core_module.model.Name

inline fun <reified T : ViewModel> Fragment.viewModel(
    crossinline provider: () -> T
) = viewModels<T> {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
    }
}

inline fun <reified T : ViewModel> Fragment.savedStateViewModel(
    crossinline provider: (SavedStateHandle) -> T
) = viewModels<T> {
    object : AbstractSavedStateViewModelFactory(this, Bundle()) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T =
            provider(handle) as T
    }
}

val DIFF_UTIL_NAMES = object : DiffUtil.ItemCallback<Name>() {
    override fun areItemsTheSame(oldItem: Name, newItem: Name): Boolean =
        oldItem.arabicName == newItem.arabicName

    override fun areContentsTheSame(oldItem: Name, newItem: Name): Boolean {
        return oldItem.arabicName == newItem.arabicName
                && oldItem.englishNameMeaning == newItem.englishNameMeaning
                && oldItem.number == newItem.number
                && oldItem.transliteration == newItem.transliteration
    }
}