package dev.ufadevconf.application

data class ParcelFitResult(
    val pickupPointCode: String,
    val trackingNumber: String,
    val fits: Boolean
)
