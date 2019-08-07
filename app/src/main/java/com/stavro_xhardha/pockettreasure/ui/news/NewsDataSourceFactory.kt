//package com.stavro_xhardha.pockettreasure.ui.news
//
//import androidx.lifecycle.MutableLiveData
//import androidx.paging.DataSource
//import com.stavro_xhardha.pockettreasure.model.News
//import javax.inject.Inject
//
//class NewsDataSourceFactory @Inject constructor(private val newsDataSource: NewsDataSource) :
//    DataSource.Factory<Int, News>() {
//    val mutableDataSource: MutableLiveData<NewsDataSource> = MutableLiveData()
//    private lateinit var mNewsDataSource: NewsDataSource
//
//    override fun create(): DataSource<Int, News> {
//        mNewsDataSource = newsDataSource
//        mutableDataSource.postValue(mNewsDataSource)
//        return newsDataSource
//    }
//}