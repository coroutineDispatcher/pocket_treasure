package com.sxhardha.quran_module.network

import com.sxhardha.quran_module.model.QuranResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface QuranApi {
    @GET
    suspend fun getQuranDataAsync(@Url baseUrl: String): Response<QuranResponse>
}