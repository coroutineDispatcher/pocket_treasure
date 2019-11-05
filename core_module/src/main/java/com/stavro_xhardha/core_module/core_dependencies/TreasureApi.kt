package com.stavro_xhardha.core_module.core_dependencies

import com.stavro_xhardha.core_module.model.PrayerTimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TreasureApi {
    @GET("timingsByCity")
    suspend fun getPrayerTimesTodayAsync(
        @Query("city") city: String?,
        @Query("country") country: String?,
        @Query("adjustment") adjustment: Int = 1,
        @Query("method") method: Int = 3
    ): Response<PrayerTimeResponse>
}