package com.stavro_xhardha.core_module.dependency_injection.modules

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module


@AssistedModule
@Module(includes = [AssistedInject_HomeViewModelModule::class])
abstract class ViewModelModule