package com.stavro_xhardha.core_module.dependency_injection.modules

import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
object DispatchersModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers =
        AppCoroutineDispatchers(
            ioDispatcher = Dispatchers.IO,
            mainDispatcher = Dispatchers.Main
        )
}