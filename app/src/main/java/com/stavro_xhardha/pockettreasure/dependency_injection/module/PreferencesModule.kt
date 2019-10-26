package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import com.stavro_xhardha.core_module.brain.SHARED_PREFERENCES_TAG
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PreferencesModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideRocket(context: Application): Rocket =
        Rocket.launch(context, SHARED_PREFERENCES_TAG)

}
