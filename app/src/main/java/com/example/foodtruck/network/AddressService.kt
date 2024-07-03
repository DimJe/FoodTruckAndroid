package com.example.foodtruck.network

import com.example.foodtruck.model.AddressResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressService {
    @GET("v2/local/geo/coord2address.json")
    fun getAddress(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double,
        @Query("input_coord") inputCoord: String = "WGS84"
    ): ResultType<AddressResponse>
}