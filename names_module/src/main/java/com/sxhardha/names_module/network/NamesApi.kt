package com.sxhardha.names_module.network

import com.sxhardha.names_module.model.NameResponse
import retrofit2.Response
import retrofit2.http.GET

interface NamesApi {
    @GET("asmaAlHusna")
    suspend fun getNintyNineNamesAsync(): Response<NameResponse>
}