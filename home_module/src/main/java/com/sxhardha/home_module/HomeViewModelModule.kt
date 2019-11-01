package com.sxhardha.home_module

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_HomeViewModelModule::class])
abstract class HomeViewModelModule