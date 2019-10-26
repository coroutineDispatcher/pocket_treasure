package com.stavro_xhardha.pockettreasure.dependency_injection.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stavro_xhardha.compass_module.CompassViewModel
import com.stavro_xhardha.pockettreasure.brain.GeneralViewModelFactory
import com.stavro_xhardha.pockettreasure.dependency_injection.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class VMModule {
    @Binds
    @Singleton
    abstract fun bindsViewModelFactory(factory: GeneralViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CompassViewModel::class)
    @Singleton
    abstract fun bindCompassViewModel(compassViewModel: CompassViewModel): ViewModel
}