package com.example.foodtruck.repository

import com.example.foodtruck.model.TruckRequest
import com.example.foodtruck.model.TruckResponse
import com.example.foodtruck.network.ResultType
import com.example.foodtruck.network.TruckService
import javax.inject.Inject

class TruckRepository @Inject constructor(
    private val truckService: TruckService
) {
    suspend fun getTruckList(): ResultType<List<TruckResponse>> {
        val response = try {
            truckService.getTruckData()
        } catch (e: Exception) {
            return ResultType.Error(e.message!!)
        }
        return ResultType.Success(response.body()!!)
    }
    suspend fun postTruck(truckRequest: TruckRequest) {
        truckService.postTruck(truckRequest)
    }

}