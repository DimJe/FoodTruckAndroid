package com.example.foodtruck.viewmodel

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodtruck.model.TruckRequest
import com.example.foodtruck.model.TruckResponse
import com.example.foodtruck.network.ResultType
import com.example.foodtruck.repository.AddressRepository
import com.example.foodtruck.repository.TruckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TruckViewModel @Inject constructor(
    private val truckRepository: TruckRepository,
    private val addressRepository: AddressRepository
): ViewModel() {

    var location = mutableStateOf<Location?>(null)
    var truckList = mutableStateOf<List<TruckResponse>>(emptyList())
    var address = mutableStateOf("")
    fun getTruckList() {
        viewModelScope.launch {
            val result = truckRepository.getTruckList()
            when(result){
                is ResultType.Success -> {
                    truckList.value = result.data!!
                }
                is ResultType.Error -> {
                    //에러 처리
                }
            }
        }
    }
    fun postTruck(truckRequest: TruckRequest){
        viewModelScope.launch {
            truckRepository.postTruck(truckRequest)
        }
    }
    fun getAddress(longitude: Double, latitude: Double) {
        viewModelScope.launch {
            val result = addressRepository.getAddress(longitude, latitude)
            Log.d("test", "getAddress: ${result.data}")
            when(result) {
                is ResultType.Success -> {
                    if(result.data?.documents?.get(0)?.roadAddress != null) address.value = result.data.documents.get(0).roadAddress!!.addressName!!
                    else address.value = result.data?.documents?.get(0)?.address!!.addressName!!
                }

                is ResultType.Error -> {
                    Log.d("test", "getAddress: ${result.message}")
                }
            }
        }
    }

}