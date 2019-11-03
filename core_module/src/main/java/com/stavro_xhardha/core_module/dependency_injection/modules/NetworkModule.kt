package com.stavro_xhardha.core_module.dependency_injection.modules

import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [BaseNetworkModule::class])
object NetworkModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideTreasureApi(retrofit: Retrofit): TreasureApi =
        retrofit.create(TreasureApi::class.java)
}