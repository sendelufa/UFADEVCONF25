package dev.ufadevconf.domain

interface PickupPointRepository {
    fun save(pickupPoint: PickupPoint)
    fun findByCode(code: String): PickupPoint?
}
