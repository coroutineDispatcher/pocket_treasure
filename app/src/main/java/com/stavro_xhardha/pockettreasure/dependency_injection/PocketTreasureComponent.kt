package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import android.app.WallpaperManager
import android.media.MediaPlayer
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.background.OfflinePrayerScheduler
import com.stavro_xhardha.pockettreasure.dependency_injection.module.*
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.pockettreasure.ui.compass.CompassFragment
import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryFragment
import com.stavro_xhardha.pockettreasure.ui.home.HomeFragment
import com.stavro_xhardha.pockettreasure.ui.names.NamesFragment
import com.stavro_xhardha.pockettreasure.ui.quran.QuranFragment
import com.stavro_xhardha.pockettreasure.ui.quran.aya.AyaFragment
import com.stavro_xhardha.pockettreasure.ui.settings.SettingsFragment
import com.stavro_xhardha.pockettreasure.ui.setup.SetupFragment
import com.stavro_xhardha.rocket.Rocket
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [NetworkModule::class, PreferencesModule::class, DatabaseModule::class, MediaModule::class,
        OfflineSchedulerModule::class, ViewModelModule::class]
)
interface PocketTreasureComponent {
    fun getTreasureApi(): TreasureApi

    fun getSharedPreferences(): Rocket

    fun treasureDatabase(): TreasureDatabase

    fun picasso(): Picasso

    fun wallpaperManager(): WallpaperManager

    fun offlineScheduler(): OfflinePrayerScheduler

    fun mediaPlayer(): MediaPlayer

    fun inject(fragment: GalleryFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: NamesFragment)
    //    fun inject(fragment: NewsFragment)
    fun inject(fragment: AyaFragment)

    fun inject(fragment: QuranFragment)
    fun inject(fragment: CompassFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: SetupFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance pocketTreasureApplication: Application): PocketTreasureComponent
    }
}