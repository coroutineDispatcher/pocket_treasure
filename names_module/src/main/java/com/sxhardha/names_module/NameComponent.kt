package com.sxhardha.names_module

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import dagger.Component

@FragmentScoped
@Component(
    modules = [NamesViewModelModule::class],
    dependencies = [CoreComponent::class]
)
interface NameComponent {
    val namesViewModelFactory: NamesViewModel.Factory

    @Component.Factory
    interface NamesFactory {
        fun create(coreComponent: CoreComponent): NameComponent
    }
}