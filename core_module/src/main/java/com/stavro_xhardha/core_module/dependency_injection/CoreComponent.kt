package com.stavro_xhardha.core_module.dependency_injection

import android.app.Application
import android.app.WallpaperManager
import android.media.MediaPlayer
import com.squareup.picasso.Picasso
import com.stavro_xhardha.core_module.core_dependencies.*
import com.stavro_xhardha.core_module.dependency_injection.modules.*
import com.stavro_xhardha.core_module.ui.HomeViewModel
import com.stavro_xhardha.rocket.Rocket
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, PreferencesModule::class, MediaModule::class, DispatchersModule::class, ViewModelModule::class])
interface CoreComponent {
    val treasureApi: TreasureApi

    val application: Application

    val rocket: Rocket

    val picasso: Picasso

    val wallpaperManager: WallpaperManager

    val mediaPlayer: MediaPlayer

    val appCoroutineDispatchers: AppCoroutineDispatchers

    val retrofit: Retrofit

    val homeViewModelFactory: HomeViewModel.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): CoreComponent
    }
}