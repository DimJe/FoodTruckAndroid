package com.example.foodtruck.model

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    val documents: List<Document>?
)
data class Document(
    @SerializedName("road_address")
    val roadAddress: Address?
)

data class Address(
    val addressName: String?
)