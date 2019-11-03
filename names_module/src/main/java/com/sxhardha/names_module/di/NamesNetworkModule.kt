package com.sxhardha.names_module.di

import com.stavro_xhardha.core_module.dependency_injection.FragmentScoped
import com.stavro_xhardha.core_module.dependency_injection.modules.BaseNetworkModule
import com.sxhardha.names_module.network.NamesApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [BaseNetworkModule::class])
object NamesNetworkModule {

    @JvmStatic
    @Provides
    @FragmentScoped
    fun provideNamesApi(retrofit: Retrofit): NamesApi = retrofit.create(NamesApi::class.java)
}
