package com.example.foodtruck.model

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    val documents: List<Document>?
)
data class Document(
    @SerializedName("road_address")
    val roadAddress: RoadAddress?,
    val address: Address
)

data class RoadAddress(
    @SerializedName("address_name")
    val addressName: String?
)
data class Address(
    @SerializedName("address_name")
    val addressName: String?
)