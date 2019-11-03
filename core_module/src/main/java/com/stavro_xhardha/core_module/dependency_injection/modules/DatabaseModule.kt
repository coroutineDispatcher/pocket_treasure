package com.stavro_xhardha.core_module.dependency_injection.modules

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.core_module.brain.TREASURE_DATABASE_NAME
import com.stavro_xhardha.core_module.core_dependencies.AyasDao
import com.stavro_xhardha.core_module.core_dependencies.SurahsDao
import com.stavro_xhardha.core_module.core_dependencies.TreasureDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideRoomDatabase(context: Application): TreasureDatabase = Room.databaseBuilder(
        context,
        TreasureDatabase::class.java, TREASURE_DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @JvmStatic
    @Provides
    @Singleton
    fun providesSurahsDao(database: TreasureDatabase): SurahsDao = database.surahsDao()

    @JvmStatic
    @Provides
    @Singleton
    fun providesAyahDao(database: TreasureDatabase): AyasDao = database.ayasDao()
}