package com.sxhardha.setup_module

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import dagger.Component

@Component(dependencies = [CoreComponent::class], modules = [SetupViewModelModule::class])
@FragmentScoped
interface SetupComponent {

    @Component.Factory
    interface SetupFactory {
        fun create(coreComponent: CoreComponent): SetupComponent
    }
}