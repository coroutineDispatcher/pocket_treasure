package com.stavro_xhardha.pockettreasure.ui.names

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.NamesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class NamesFragmentModule {

    @Provides
    @FragmentScope
    fun provideViewModelFactory(
        provider: Provider<NamesViewModel>
    ): NamesViewModelProviderFactory {
        return NamesViewModelProviderFactory(provider)
    }

    @Provides
    @FragmentScope
    fun provideNamesRepository(treasureApi: TreasureApi, namesDao: NamesDao): NamesRepository =
        NamesRepository(treasureApi, namesDao)

    @Provides
    @FragmentScope
    fun provideNamesDao(treasureDatabase: TreasureDatabase): NamesDao = treasureDatabase.namesDao()
}