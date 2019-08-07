//package com.stavro_xhardha.pockettreasure.ui.news
//
//import androidx.lifecycle.MutableLiveData
//import androidx.paging.PageKeyedDataSource
//import com.stavro_xhardha.pockettreasure.brain.*
//import com.stavro_xhardha.pockettreasure.model.News
//import com.stavro_xhardha.pockettreasure.model.NewsResponse
//import com.stavro_xhardha.pockettreasure.network.TreasureApi
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.launch
//import retrofit2.Response
//import javax.inject.Inject
//
//class NewsDataSource @Inject constructor(val treasureApi: TreasureApi) : PageKeyedDataSource<Int, News>() {
//
//    private val completableJob = Job()
//    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)
//
//    private var retry: (() -> Any)? = null
//    val networkState = MutableLiveData<NetworkState>()
//    val initialLoad = MutableLiveData<NetworkState>()
//
//    fun retryAllFailed() {
//        val prevRetry = retry
//        retry = null
//        prevRetry?.invoke()
//    }
//
//    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
//        coroutineScope.launch(Dispatchers.IO) {
//            try {
//                initialLoad.postValue(NetworkState.LOADING)
//                val primaryNewsResponse = callLatestNewsAsync(1)
//                if (primaryNewsResponse.isSuccessful) {
//                    initialLoad.postValue(NetworkState.LOADED)
//                    callback.onResult(primaryNewsResponse.body()!!.articles, null, 2)
//                } else {
//                    retry = {
//                        loadInitial(params, callback)
//                    }
//                    val error = NetworkState.error("Network error")
//                    initialLoad.postValue(error)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                retry = {
//                    loadInitial(params, callback)
//                }
//                val error = NetworkState.error("Network error")
//                initialLoad.postValue(error)
//            }
//        }
//    }
//
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
//        coroutineScope.launch(Dispatchers.IO) {
//            try {
//                networkState.postValue(NetworkState.LOADING)
//                val primaryNewsResponse = callLatestNewsAsync(params.key)
//                if (primaryNewsResponse.isSuccessful) {
//                    retry = null
//                    callback.onResult(primaryNewsResponse.body()!!.articles, params.key.inc())
//                    networkState.postValue(NetworkState.LOADED)
//                } else {
//                    retry = {
//                        loadAfter(params, callback)
//                    }
//                    networkState.postValue(NetworkState.error("Network Error"))
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                retry = {
//                    loadAfter(params, callback)
//                }
//                networkState.postValue(NetworkState.error("Network Error"))
//            }
//        }
//    }
//
//    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
//    }
//
//    private suspend fun callLatestNewsAsync(pageNumber: Int): Response<NewsResponse> = treasureApi.getLatestNewsAsync(
//        NEWS_BASE_URL, SEARCH_KEY_WORD, SEARCH_NEWS_API_KEY, pageNumber, INITIAL_PAGE_SIZE
//    )
//
//    fun clearCoroutineJobs() {
//        completableJob.cancel()
//    }
//}