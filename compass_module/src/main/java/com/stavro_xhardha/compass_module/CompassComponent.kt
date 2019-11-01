package com.stavro_xhardha.compass_module

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import dagger.Component

@FragmentScoped
@Component(
    dependencies = [CoreComponent::class]
)
interface CompassComponent {

    val compassViewModel: CompassViewModel

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): CompassComponent
    }
}