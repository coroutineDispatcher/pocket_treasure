package com.stavro_xhardha.pockettreasure.dependency_injection.module

import com.stavro_xhardha.pockettreasure.model.AppCoroutineDispatchers
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
object DispatchersModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        ioDispatcher = Dispatchers.IO,
        mainDispatcher = Dispatchers.Main,
        unconfirmedDispatcher = Dispatchers.Unconfined,
        defaultDispatcher = Dispatchers.Default
    )
}