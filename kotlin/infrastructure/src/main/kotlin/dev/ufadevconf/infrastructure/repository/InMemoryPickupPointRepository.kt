package dev.ufadevconf.infrastructure.repository

import dev.ufadevconf.domain.PickupPoint
import dev.ufadevconf.domain.PickupPointRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryPickupPointRepository : PickupPointRepository {

    private val storage = ConcurrentHashMap<String, PickupPoint>()

    override fun save(pickupPoint: PickupPoint) {
        storage[pickupPoint.code] = pickupPoint
    }

    override fun findByCode(code: String): PickupPoint? = storage[code]
}
