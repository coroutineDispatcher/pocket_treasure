package com.stavro_xhardha.core_module.dependency_injection

import android.app.Application
import android.app.WallpaperManager
import android.media.MediaPlayer
import com.squareup.picasso.Picasso
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import com.stavro_xhardha.core_module.core_dependencies.NamesDao
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import com.stavro_xhardha.core_module.core_dependencies.TreasureDatabase
import com.stavro_xhardha.core_module.dependency_injection.modules.*
import com.stavro_xhardha.rocket.Rocket
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class, PreferencesModule::class, MediaModule::class, DispatchersModule::class, ViewModelModule::class])
interface CoreComponent {
    val treasureApi: TreasureApi

    val rocket: Rocket

    val picasso: Picasso

    val wallpaperManager: WallpaperManager

    val mediaPlayer: MediaPlayer

    val appCoroutineDispatchers: AppCoroutineDispatchers

    val treasureDatabase: TreasureDatabase

    val namesDao: NamesDao

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): CoreComponent
    }
}