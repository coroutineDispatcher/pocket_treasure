package com.stavro_xhardha.core_module.dependency_injection.modules

import com.stavro_xhardha.core_module.brain.PRAYER_API_BASE_URL
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module(includes = [InterceptorModule::class])
object NetworkModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideTreasureApi(retrofit: Retrofit): TreasureApi =
        retrofit.create(TreasureApi::class.java)

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(PRAYER_API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
}