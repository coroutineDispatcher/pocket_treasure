package com.stavro_xhardha.pockettreasure.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import com.stavro_xhardha.core_module.model.UnsplashResponse
import com.stavro_xhardha.core_module.model.UnsplashResult
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

class GalleryDataSource @Inject constructor(val treasureApi: TreasureApi, val appCoroutineDispatchers: AppCoroutineDispatchers) :
    PageKeyedDataSource<Int, UnsplashResult>() {

    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(appCoroutineDispatchers.ioDispatcher + completableJob)

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashResult>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashResult>) {
        coroutineScope.launch(appCoroutineDispatchers.ioDispatcher) {
            try {
                incrementIdlingResource()
                networkState.postValue(NetworkState.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(params.key)
                if (primaryUnsplashResponse.code() == 200) {
                    retry = null
                    callback.onResult(primaryUnsplashResponse.body()!!.results, params.key.inc())
                    networkState.postValue(NetworkState.LOADED)
                    withContext(Dispatchers.Main) {
                        decrementIdlingResource()
                    }
                } else {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error("Network Error"))
                    withContext(Dispatchers.Main) {
                        decrementIdlingResource()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error("Network Error"))
                withContext(Dispatchers.Main) {
                    decrementIdlingResource()
                }
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UnsplashResult>) {
        coroutineScope.launch(appCoroutineDispatchers.ioDispatcher) {
            try {
                incrementIdlingResource()
                initialLoad.postValue(NetworkState.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(1)
                if (primaryUnsplashResponse.code() == 200) {
                    initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(primaryUnsplashResponse.body()!!.results, null, 2)
                    withContext(Dispatchers.Main) {
                        decrementIdlingResource()
                    }
                } else {
                    retry = {
                        loadInitial(params, callback)
                    }
                    val error = NetworkState.error("Network error")
                    initialLoad.postValue(error)
                    withContext(Dispatchers.Main) {
                        decrementIdlingResource()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error("Network error")
                initialLoad.postValue(error)
                withContext(Dispatchers.Main) {
                    decrementIdlingResource()
                }
            }
        }
    }

    private suspend fun getPhotosFromUsplashAPI(pageNumber: Int): Response<UnsplashResponse> =
        treasureApi.getUnsplashImagesAsync(
            UNSPLASH_BASE_URL,
            UNPLASH_QUERY_VALUE, pageNumber,
            INITIAL_PAGE_SIZE,
            CLIENT_ID,
            CLIENT_SECRET
        )

    fun clearCoroutineJobs() {
        completableJob.cancel()
    }
}