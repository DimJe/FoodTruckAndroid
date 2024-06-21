package com.example.foodtruck.model


data class TruckResponse(

    var id: Long? = null,
    var name: String,
    var heartCount: Int,
    var latitude: Double,
    var longitude: Double,
    var menu: List<String>,
    var review: List<Review>,
    var openDay: Short,
    var report: Int,
    var address: String
)

data class Review(

    var userName: String,
    var content: String? = "",
    var point: Float,
    var day: String
)
