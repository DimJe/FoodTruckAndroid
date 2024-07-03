package com.example.foodtruck.viewmodel

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodtruck.model.TruckRequest
import com.example.foodtruck.model.TruckResponse
import com.example.foodtruck.network.ResultType
import com.example.foodtruck.repository.TruckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TruckViewModel @Inject constructor(
    private val truckRepository: TruckRepository
): ViewModel() {

    var location = mutableStateOf<Location?>(null)
    var truckList = mutableStateOf<List<TruckResponse>>(emptyList())

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

}