package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.pockettreasure.brain.MIGRATION_1_2
import com.stavro_xhardha.pockettreasure.brain.TREASURE_DATABASE_NAME
import com.stavro_xhardha.pockettreasure.dependency_injection.ApplicationScope
import com.stavro_xhardha.pockettreasure.room_db.*
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideRoomDatabase(context: Application): TreasureDatabase = Room.databaseBuilder(
        context,
        TreasureDatabase::class.java, TREASURE_DATABASE_NAME
    ).addMigrations(MIGRATION_1_2).build()

    @JvmStatic
    @Provides
    @ApplicationScope
    fun providesNamesDao(database: TreasureDatabase): NamesDao = database.namesDao()

    @JvmStatic
    @Provides
    @ApplicationScope
    fun providesSurahsDao(database: TreasureDatabase): SurahsDao = database.surahsDao()

    @JvmStatic
    @Provides
    @ApplicationScope
    fun providesAyahDao(database: TreasureDatabase): AyasDao = database.ayasDao()

    @JvmStatic
    @Provides
    @ApplicationScope
    fun providesPrayerTimesDao(database: TreasureDatabase): PrayerTimesDao = database.prayerTimesDao()
}