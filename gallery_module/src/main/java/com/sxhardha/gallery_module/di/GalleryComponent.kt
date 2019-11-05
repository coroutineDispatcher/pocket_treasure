package com.sxhardha.gallery_module.di

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.gallery_module.gallery.GalleryViewModel
import com.sxhardha.gallery_module.network.GalleryApi
import dagger.Component

@Component(dependencies = [CoreComponent::class], modules = [GalleryNetworkModule::class])
@FragmentScoped
interface GalleryComponent {
    val galleryViewModel: GalleryViewModel

    val galleryApi: GalleryApi
}