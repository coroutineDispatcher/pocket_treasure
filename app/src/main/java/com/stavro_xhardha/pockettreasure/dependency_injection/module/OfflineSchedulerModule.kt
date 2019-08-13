package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import com.squareup.inject.assisted.AssistedInject
import com.squareup.inject.assisted.dagger2.AssistedModule
import com.stavro_xhardha.pockettreasure.background.OfflinePrayerScheduler
import com.stavro_xhardha.pockettreasure.dependency_injection.ApplicationScope
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides

@Module(includes = [PreferencesModule::class, DatabaseModule::class])
object OfflineSchedulerModule {

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideOfflineScheduler(
        rocket: Rocket,
        context: Application,
        treasureDatabase: TreasureDatabase
    ) =
        OfflinePrayerScheduler(rocket, context, treasureDatabase.prayerTimesDao())
}