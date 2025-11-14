package dev.ufadevconf.application

class PickupPointNotFoundException(code: String) : RuntimeException("Pickup point not found: $code")
