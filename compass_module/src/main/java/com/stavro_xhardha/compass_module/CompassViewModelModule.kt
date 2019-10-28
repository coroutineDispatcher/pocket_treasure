package com.stavro_xhardha.compass_module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class CompassViewModelModule {
    @Binds
    abstract fun bindCompassViewModel(compassViewModel: CompassViewModel): ViewModel
}
