package com.sxhardha.quran_module.aya

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import dagger.Component

@Component(dependencies = [CoreComponent::class])
@FragmentScoped
interface AyaComponent {

    val ayaViewModel: AyaViewModel

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AyaComponent
    }
}