package com.example.foodtruck.network

import com.example.foodtruck.model.AddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AddressService {
    @GET("/v2/local/geo/coord2address.json")
    suspend fun getAddress(
        @Header("Authorization") REST_API_KEY: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double,
    ): Response<AddressResponse>
}