package com.sxhardha.home_module

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import dagger.Component

@FragmentScoped
@Component(dependencies = [CoreComponent::class], modules = [HomeViewModelModule::class])
interface HomeComponent {

    val homeViewModelFactory: HomeViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): HomeComponent
    }
}