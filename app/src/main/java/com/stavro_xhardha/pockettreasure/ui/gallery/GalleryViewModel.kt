package com.stavro_xhardha.pockettreasure.ui.gallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.UnsplashResult

class GalleryViewModel @AssistedInject constructor(
    private val galleryDataSourceFactory: GalleryDataSourceFactory,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): GalleryViewModel
    }

    private var listing: Listing<UnsplashResult>
    private var galleryData: LiveData<PagedList<UnsplashResult>>
    private var networkStateLiveData: LiveData<NetworkState>
    private var refreshState: LiveData<NetworkState>

    init {

        val config = buildPagedList()

        val livePagedListBuilder =
            LivePagedListBuilder(galleryDataSourceFactory, config).build()

        val refreshStateListener = switchMap(galleryDataSourceFactory.sourceLiveData) {
            it.initialLoad
        }

        listing = Listing(
            pagedList = livePagedListBuilder,
            networkState = switchMap(galleryDataSourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = {
                galleryDataSourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                galleryDataSourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshStateListener,
            clearCoroutineJobs = {
                galleryDataSourceFactory.sourceLiveData.value?.clearCoroutineJobs()
            }
        )

        galleryData = listing.pagedList
        networkStateLiveData = listing.networkState
        refreshState = listing.refreshState
    }

    fun retry() {
        if (isDebugMode)
            Log.d(APPLICATION_TAG, "RETRYING")
        listing.retry.invoke()
    }

    fun getGalleryLiveData(): LiveData<PagedList<UnsplashResult>> = galleryData

    fun getCurrentState(): LiveData<NetworkState> = networkStateLiveData

    fun getInitialState(): LiveData<NetworkState> = refreshState

    override fun onCleared() {
        super.onCleared()
        listing.clearCoroutineJobs.invoke()
    }
}