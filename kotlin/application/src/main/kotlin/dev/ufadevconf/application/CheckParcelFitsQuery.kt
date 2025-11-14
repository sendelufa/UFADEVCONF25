package dev.ufadevconf.application

data class CheckParcelFitsQuery(
    val pickupPointCode: String,
    val trackingNumber: String,
    val contentsDescription: String,
    val parcelWeightGrams: Long
)
