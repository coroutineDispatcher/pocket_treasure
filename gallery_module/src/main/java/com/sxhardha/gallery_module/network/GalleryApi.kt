package com.sxhardha.gallery_module.network

import com.sxhardha.gallery_module.model.UnsplashResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GalleryApi {
    @GET
    suspend fun getUnsplashImagesAsync(
        @Url baseUrl: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") resultPerPage: Int,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Response<UnsplashResponse>
}