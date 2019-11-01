package com.sxhardha.gallery_module.gallery

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.stavro_xhardha.core_module.model.UnsplashResult
import com.sxhardha.gallery_module.gallery.GalleryDataSource
import javax.inject.Inject

class GalleryDataSourceFactory @Inject constructor(private val galleryDataSource: GalleryDataSource) :
    DataSource.Factory<Int, UnsplashResult>() {
    val sourceLiveData: MutableLiveData<GalleryDataSource> = MutableLiveData()
    private lateinit var mGalleryDataSouce: GalleryDataSource

    override fun create(): DataSource<Int, UnsplashResult> {
        mGalleryDataSouce = galleryDataSource
        sourceLiveData.postValue(mGalleryDataSouce)
        return galleryDataSource
    }
}