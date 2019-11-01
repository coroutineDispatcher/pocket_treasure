package com.sxhardha.gallery_module.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.core_module.brain.buildPagedList
import com.stavro_xhardha.core_module.model.UnsplashResult
import com.sxhardha.gallery_module.Listing
import com.sxhardha.gallery_module.NetworkState
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val galleryDataSourceFactory: GalleryDataSourceFactory
) : ViewModel() {

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