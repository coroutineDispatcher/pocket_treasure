package com.sxhardha.gallery_module

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.gallery_module.gallery.GalleryViewModel
import dagger.Component

@Component(dependencies = [CoreComponent::class])
@FragmentScoped
interface GalleryComponent {

    val galleryViewModel: GalleryViewModel

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): GalleryComponent
    }
}