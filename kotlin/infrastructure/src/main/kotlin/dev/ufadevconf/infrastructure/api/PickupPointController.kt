package dev.ufadevconf.infrastructure.api

import dev.ufadevconf.application.CheckParcelFitsQuery
import dev.ufadevconf.application.CreatePickupPointCommand
import dev.ufadevconf.application.ParcelFitResult
import dev.ufadevconf.application.PickupPointApplicationService
import dev.ufadevconf.domain.PickupPoint
import dev.ufadevconf.domain.Weight
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/pickup-points")
class PickupPointController(
    private val applicationService: PickupPointApplicationService
) {

    @PostMapping
    fun create(@RequestBody request: CreatePickupPointRequest): PickupPointResponse {
        val minWeight = Weight.ofKilograms(request.minWeightKg)
        val maxWeight = Weight.ofKilograms(request.maxWeightKg)
        val command = CreatePickupPointCommand(
            code = request.code,
            address = request.address,
            minWeightGrams = minWeight.grams,
            maxWeightGrams = maxWeight.grams
        )
        val pickupPoint = applicationService.registerPickupPoint(command)
        return pickupPoint.toResponse()
    }

    @PostMapping("/{code}/fit-check")
    fun checkParcelFits(
        @PathVariable code: String,
        @RequestBody request: ParcelRequest
    ): ParcelFitResponse {
        val query = CheckParcelFitsQuery(
            pickupPointCode = code,
            trackingNumber = request.trackingNumber,
            contentsDescription = request.contentsDescription,
            parcelWeightGrams = request.parcelWeightGrams
        )
        val result: ParcelFitResult = applicationService.checkParcelFits(query)
        return ParcelFitResponse(result.pickupPointCode, result.trackingNumber, result.fits)
    }

    private fun PickupPoint.toResponse(): PickupPointResponse = PickupPointResponse(
        code = code,
        address = address,
        minWeightGrams = minWeight.grams,
        maxWeightGrams = maxWeight.grams
    )

    data class CreatePickupPointRequest(
        val code: String,
        val address: String,
        val minWeightKg: Double,
        val maxWeightKg: Double
    )

    data class ParcelRequest(
        val trackingNumber: String,
        val contentsDescription: String,
        val parcelWeightGrams: Long
    )

    data class PickupPointResponse(
        val code: String,
        val address: String,
        val minWeightGrams: Long,
        val maxWeightGrams: Long
    )

    data class ParcelFitResponse(
        val pickupPointCode: String,
        val trackingNumber: String,
        val fits: Boolean
    )
}
