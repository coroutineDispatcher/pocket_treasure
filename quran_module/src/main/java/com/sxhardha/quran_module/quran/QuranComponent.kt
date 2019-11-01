package com.sxhardha.quran_module.quran

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import dagger.Component

@Component(dependencies = [CoreComponent::class])
@FragmentScoped
interface QuranComponent {

    val quranViewModel: QuranViewModel

    @Component.Factory
    interface Factory {
        fun create(component: CoreComponent): QuranComponent
    }
}