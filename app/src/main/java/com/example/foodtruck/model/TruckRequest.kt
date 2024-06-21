package com.example.foodtruck.model

data class TruckRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val openDay: Short,
    val address: String
)
