package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import com.stavro_xhardha.pockettreasure.brain.SHARED_PREFERENCES_TAG
import com.stavro_xhardha.pockettreasure.dependency_injection.ApplicationScope
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides

@Module
object PreferencesModule {

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideRocket(context: Application): Rocket =
        Rocket.launch(context, SHARED_PREFERENCES_TAG)

}
