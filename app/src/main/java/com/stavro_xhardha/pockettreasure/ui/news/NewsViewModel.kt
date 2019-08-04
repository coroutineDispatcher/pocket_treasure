package com.stavro_xhardha.pockettreasure.ui.news

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.News
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    dataSourceFactory: NewsDataSourceFactory,
    val rocket: Rocket
) : ViewModel() {
    private var listing: Listing<News>
    private val _enterDialogVisibility = MutableLiveData<Boolean>()
    var enterDialogVisibility = _enterDialogVisibility
    private var newsData: LiveData<PagedList<News>>
    private var networkState: LiveData<NetworkState>
    private var refreshState: LiveData<NetworkState>

    init {

        val config = buildPagedList()

        val livePagedListBuilder =
            LivePagedListBuilder(dataSourceFactory, config).build()

        val refreshStateListener = Transformations.switchMap(dataSourceFactory.mutableDataSource) {
            it.initialLoad
        }

        listing = Listing(
            pagedList = livePagedListBuilder,
            networkState = Transformations.switchMap(dataSourceFactory.mutableDataSource) {
                it.networkState
            },
            retry = {
                dataSourceFactory.mutableDataSource.value?.retryAllFailed()
            },
            refresh = {
                dataSourceFactory.mutableDataSource.value?.invalidate()
            },
            refreshState = refreshStateListener,
            clearCoroutineJobs = {
                dataSourceFactory.mutableDataSource.value?.clearCoroutineJobs()
            }
        )

        newsData = listing.pagedList
        networkState = listing.networkState
        refreshState = listing.refreshState

        viewModelScope.launch {
            checkDialogVisibility()
        }
    }

    private suspend fun checkDialogVisibility() {
        if (!rocket.readBoolean(HAS_ONCE_ENTERED_NEWS)) {
            _enterDialogVisibility.postValue(true)
            rocket.writeBoolean(HAS_ONCE_ENTERED_NEWS, true)
        }
    }

    fun retry() {
        if (isDebugMode)
            Log.d(APPLICATION_TAG, "RETRYING")
        listing.retry.invoke()
    }

    override fun onCleared() {
        super.onCleared()
        listing.clearCoroutineJobs.invoke()
    }

    fun newsDataList(): LiveData<PagedList<News>> = newsData

    fun currentNetworkState(): LiveData<NetworkState> = networkState

    fun initialNetworkState(): LiveData<NetworkState> = refreshState

    fun turnOfNextVisibility() {
        _enterDialogVisibility.value = false
    }
}