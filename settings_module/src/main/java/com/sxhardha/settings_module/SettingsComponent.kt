package com.sxhardha.settings_module

import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import dagger.Component

@FragmentScoped
@Component(dependencies = [CoreComponent::class], modules = [SettingsModule::class])
interface SettingsComponent {

    val settingsViewModel: SettingsViewModel

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): SettingsComponent
    }
}