package dev.ufadevconf.domain

data class Parcel(
    val trackingNumber: String,
    val weight: Weight,
    val contentsDescription: String
)
