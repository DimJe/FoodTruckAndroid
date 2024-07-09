package com.example.foodtruck.repository

import android.util.Log
import com.example.foodtruck.model.AddressResponse
import com.example.foodtruck.network.AddressService
import com.example.foodtruck.network.ResultType
import javax.inject.Inject
import com.example.foodtruck.BuildConfig

class AddressRepository @Inject constructor(
    private val addressService: AddressService
) {
    suspend fun getAddress(longitude: Double, latitude: Double): ResultType<AddressResponse> {
        val response = try {
            addressService.getAddress(BuildConfig.KAKAO_API_KEY,longitude,latitude)
        }catch (e: Exception) {
            return ResultType.Error(e.message!!)
        }
        Log.d("test", "getAddress: ${response.raw()}")
        return ResultType.Success(response.body()!!)
    }
}