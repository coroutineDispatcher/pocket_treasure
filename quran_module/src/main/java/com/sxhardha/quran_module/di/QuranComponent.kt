package com.sxhardha.quran_module.di

import android.app.Application
import com.stavro_xhardha.core_module.dependency_injection.CoreComponent
import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.quran_module.database.AyasDao
import com.sxhardha.quran_module.database.SurahsDao
import com.sxhardha.quran_module.network.QuranApi
import com.sxhardha.quran_module.ui.aya.AyaViewModel
import com.sxhardha.quran_module.ui.quran.QuranViewModel
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

@Component(
    dependencies = [CoreComponent::class],
    modules = [QuranDatabaseModule::class, QuranApiModule::class]
)
@FragmentScoped
interface QuranComponent {
    val quranViewModel: QuranViewModel

    val ayaViewModel: AyaViewModel

    val quranApi: QuranApi

    val surahsDao: SurahsDao

    val ayasDao: AyasDao
}