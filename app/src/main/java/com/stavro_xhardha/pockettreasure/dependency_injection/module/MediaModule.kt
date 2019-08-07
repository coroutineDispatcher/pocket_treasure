package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import android.app.WallpaperManager
import android.media.MediaPlayer
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.dependency_injection.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
object MediaModule {

    @JvmStatic
    @Provides
    @ApplicationScope
    fun providePicasso(): Picasso = Picasso.get()

    @JvmStatic
    @Provides
    fun provideWallpaperManager(application: Application): WallpaperManager = WallpaperManager.getInstance(application)

    @JvmStatic
    @Provides
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()
}