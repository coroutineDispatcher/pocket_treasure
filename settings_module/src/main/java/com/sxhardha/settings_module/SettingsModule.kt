package com.sxhardha.settings_module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class SettingsModule {
    @Binds
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel
}
