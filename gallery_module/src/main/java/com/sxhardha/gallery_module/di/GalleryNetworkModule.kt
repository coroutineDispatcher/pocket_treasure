package com.sxhardha.gallery_module.di

import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.gallery_module.network.GalleryApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class GalleryNetworkModule(private val retrofit: Retrofit) {

    @Provides
    @FragmentScoped
    fun provideGalleryApi(): GalleryApi = retrofit.create(GalleryApi::class.java)
}
