package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.pockettreasure.brain.TREASURE_DATABASE_NAME
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import com.stavro_xhardha.pockettreasure.room_db.NamesDao
import com.stavro_xhardha.pockettreasure.room_db.SurahsDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
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
    fun providesNamesDao(database: TreasureDatabase): NamesDao = database.namesDao()

    @JvmStatic
    @Provides
    @Singleton
    fun providesSurahsDao(database: TreasureDatabase): SurahsDao = database.surahsDao()

    @JvmStatic
    @Provides
    @Singleton
    fun providesAyahDao(database: TreasureDatabase): AyasDao = database.ayasDao()
}