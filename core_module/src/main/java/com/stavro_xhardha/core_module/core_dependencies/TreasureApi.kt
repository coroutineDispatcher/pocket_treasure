package com.stavro_xhardha.core_module.core_dependencies

import com.stavro_xhardha.core_module.model.NameResponse
import com.stavro_xhardha.core_module.model.PrayerTimeResponse
import com.stavro_xhardha.core_module.model.QuranResponse
import com.stavro_xhardha.core_module.model.UnsplashResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface TreasureApi {

    @GET("asmaAlHusna")
    suspend fun getNintyNineNamesAsync(): Response<NameResponse>

    @GET("timingsByCity")
    suspend fun getPrayerTimesTodayAsync(
        @Query("city") city: String?,
        @Query("country") country: String?,
        @Query("adjustment") adjustment: Int = 1,
        @Query("method") method: Int = 3
    ): Response<PrayerTimeResponse>

    @GET
    suspend fun getUnsplashImagesAsync(
        @Url baseUrl: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") resultPerPage: Int,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Response<UnsplashResponse>

    @GET
    suspend fun getQuranDataAsync(@Url baseUrl: String): Response<QuranResponse>
}