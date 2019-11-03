package com.sxhardha.names_module.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_NamesViewModelModule::class])
abstract class NamesViewModelModule
