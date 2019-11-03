package com.sxhardha.names_module.di

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.names_module.db.NamesDao
import com.sxhardha.names_module.db.NamesDatabase
import dagger.Module
import dagger.Provides

@Module
object NamesDatabaseModule {

    @JvmStatic
    @FragmentScoped
    @Provides
    fun provideNamesDataBas(application: Application): NamesDatabase =
        Room.databaseBuilder(
            application,
            NamesDatabase::class.java,
            "names_db"
        ).fallbackToDestructiveMigration().build()

    @JvmStatic
    @FragmentScoped
    @Provides
    fun provideNamesDao(namesDatabase: NamesDatabase): NamesDao = namesDatabase.namesDao()

}