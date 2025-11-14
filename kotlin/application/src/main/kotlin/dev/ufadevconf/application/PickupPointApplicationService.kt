package dev.ufadevconf.application

import dev.ufadevconf.domain.Parcel
import dev.ufadevconf.domain.PickupPoint
import dev.ufadevconf.domain.PickupPointRepository
import dev.ufadevconf.domain.Weight

class PickupPointApplicationService(
    private val pickupPointRepository: PickupPointRepository
) {
    fun registerPickupPoint(command: CreatePickupPointCommand): PickupPoint {
        val pickupPoint = PickupPoint(
            code = command.code,
            address = command.address,
            minWeight = Weight(command.minWeightGrams),
            maxWeight = Weight(command.maxWeightGrams)
        )
        pickupPointRepository.save(pickupPoint)
        return pickupPoint
    }

    fun checkParcelFits(query: CheckParcelFitsQuery): ParcelFitResult {
        val pickupPoint = pickupPointRepository.findByCode(query.pickupPointCode)
            ?: throw PickupPointNotFoundException(query.pickupPointCode)
        val parcel = Parcel(
            trackingNumber = query.trackingNumber,
            weight = Weight(query.parcelWeightGrams),
            contentsDescription = query.contentsDescription
        )
        return ParcelFitResult(
            pickupPointCode = pickupPoint.code,
            trackingNumber = parcel.trackingNumber,
            fits = pickupPoint.canAccept(parcel)
        )
    }
}
