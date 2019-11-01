@file:Suppress("UNCHECKED_CAST")

package com.stavro_xhardha.pockettreasure.brain

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.stavro_xhardha.core_module.brain.INITIAL_PAGE_SIZE
import com.stavro_xhardha.core_module.model.*
import com.stavro_xhardha.pockettreasure.BuildConfig
import com.sxhardha.smoothie.Smoothie

val isDebugMode: Boolean = BuildConfig.DEBUG

fun buildPagedList() = PagedList.Config.Builder()
    .setPageSize(INITIAL_PAGE_SIZE)
    .setEnablePlaceholders(false)
    .build()

val DIFF_UTIL_GALLERY = object : DiffUtil.ItemCallback<UnsplashResult>() {
    override fun areItemsTheSame(oldItem: UnsplashResult, newItem: UnsplashResult): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UnsplashResult, newItem: UnsplashResult): Boolean =
        oldItem.id == newItem.id && oldItem.description == newItem.description
                && oldItem.altDescription == newItem.description
                && oldItem.photoUrls == newItem.photoUrls
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

fun incrementIdlingResource() {
    if (isDebugMode)
        Smoothie.startProcess()
}

fun decrementIdlingResource() {
    if (isDebugMode)
        Smoothie.endProcess()
}

inline fun <reified T : ViewModel> Fragment.viewModel(
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