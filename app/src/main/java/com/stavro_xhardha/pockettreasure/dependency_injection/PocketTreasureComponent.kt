package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import android.app.WallpaperManager
import android.media.MediaPlayer
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.dependency_injection.module.*
import com.stavro_xhardha.pockettreasure.model.AppCoroutineDispatchers
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.pockettreasure.ui.compass.CompassViewModel
import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryViewModel
import com.stavro_xhardha.pockettreasure.ui.home.HomeViewModel
import com.stavro_xhardha.pockettreasure.ui.names.NamesViewModel
import com.stavro_xhardha.pockettreasure.ui.quran.QuranViewModel
import com.stavro_xhardha.pockettreasure.ui.quran.aya.AyaViewModel
import com.stavro_xhardha.pockettreasure.ui.settings.SettingsViewModel
import com.stavro_xhardha.pockettreasure.ui.setup.SetupViewModel
import com.stavro_xhardha.rocket.Rocket
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class, PreferencesModule::class, DatabaseModule::class, MediaModule::class, ViewModelModule::class, DispatchersModule::class]
)
interface PocketTreasureComponent {

    val galleryViewModelFactory: GalleryViewModel.Factory

    val compassViewModelFactory: CompassViewModel.Factory

    val homeViewModelFactory: HomeViewModel.Factory

    val namesViewModelFactory: NamesViewModel.Factory

    val ayaViewModelFactory: AyaViewModel.Factory

    val quranViewModelFactory: QuranViewModel.Factory

    val settingsViewModelFactory: SettingsViewModel.Factory

    val setupViewModelFactory: SetupViewModel.Factory

    val treasureApi: TreasureApi

    val getSharedPreferences: Rocket

    val treasureDatabase: TreasureDatabase

    val picasso: Picasso

    val wallpaperManager: WallpaperManager

    val mediaPlayer: MediaPlayer

    val appCoroutineDispatchers: AppCoroutineDispatchers

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance pocketTreasureApplication: Application): PocketTreasureComponent
    }
}