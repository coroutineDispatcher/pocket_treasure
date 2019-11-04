package com.sxhardha.quran_module.di

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.quran_module.database.AyasDao
import com.sxhardha.quran_module.database.QuranDatabase
import com.sxhardha.quran_module.database.SurahsDao
import dagger.Module
import dagger.Provides

@Module
class QuranDatabaseModule(private val application: Application) {
    @Provides
    @FragmentScoped
    fun provideQuranDb(): QuranDatabase =
        Room.databaseBuilder(application, QuranDatabase::class.java, "quran_db")
            .fallbackToDestructiveMigration().build()

    @Provides
    @FragmentScoped
    fun provideQuranSurahsDao(quranDatabase: QuranDatabase): SurahsDao = quranDatabase.surahsDao()

    @Provides
    @FragmentScoped
    fun provideAyasDao(quranDatabase: QuranDatabase): AyasDao = quranDatabase.ayasDao()
}
