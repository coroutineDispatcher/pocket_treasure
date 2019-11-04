package com.sxhardha.quran_module.di

import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.sxhardha.quran_module.network.QuranApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class QuranApiModule(private val retrofit: Retrofit) {

    @Provides
    @FragmentScoped
    fun provideQuranApi(): QuranApi = retrofit.create(QuranApi::class.java)
}
