package com.sxhardha.names_module.di

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.names_module.db.NamesDao
import com.sxhardha.names_module.db.NamesDatabase
import dagger.Module
import dagger.Provides

@Module
class NamesDatabaseModule(private val application: Application) {

    @FragmentScoped
    @Provides
    fun provideNamesDataBas(): NamesDatabase =
        Room.databaseBuilder(
            application,
            NamesDatabase::class.java,
            "names_db"
        ).fallbackToDestructiveMigration().build()

    @FragmentScoped
    @Provides
    fun provideNamesDao(namesDatabase: NamesDatabase): NamesDao = namesDatabase.namesDao()

}