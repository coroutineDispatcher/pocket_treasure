package com.stavro_xhardha.core_module.brain

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import com.stavro_xhardha.core_module.model.HomePrayerTime

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
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T =
            provider(handle) as T
    }
}

val DIFF_UTIL_HOME = object : DiffUtil.ItemCallback<HomePrayerTime>() {
    override fun areItemsTheSame(oldItem: HomePrayerTime, newItem: HomePrayerTime): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: HomePrayerTime, newItem: HomePrayerTime): Boolean =
        oldItem.name == newItem.name && oldItem.time == newItem.time
}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}

class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)

    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        handler(t)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}