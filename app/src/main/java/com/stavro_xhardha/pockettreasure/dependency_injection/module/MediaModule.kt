package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import android.app.WallpaperManager
import android.media.MediaPlayer
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object MediaModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providePicasso(): Picasso = Picasso.get()

    @JvmStatic
    @Provides
    fun provideWallpaperManager(application: Application): WallpaperManager = WallpaperManager.getInstance(application)

    @JvmStatic
    @Provides
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()
}