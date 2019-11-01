package com.sxhardha.setup_module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class SetupViewModelModule {

    @Binds
    abstract fun bindSetupViewModel(setupViewModel: SetupViewModel): ViewModel
}
