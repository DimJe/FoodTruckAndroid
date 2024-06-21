package com.example.foodtruck.network

import com.example.foodtruck.model.TruckRequest
import com.example.foodtruck.model.TruckResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TruckService {

    @POST("truck/post")
    suspend fun postTruck(@Body truckRequest: TruckRequest) : Response<Void>

    @GET("truck/item")
    suspend fun getTruckData() : Response<List<TruckResponse>>
}