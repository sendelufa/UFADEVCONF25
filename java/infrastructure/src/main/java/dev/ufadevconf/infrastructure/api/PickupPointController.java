package dev.ufadevconf.infrastructure.api;

import dev.ufadevconf.application.CheckParcelFitsQuery;
import dev.ufadevconf.application.CreatePickupPointCommand;
import dev.ufadevconf.application.ParcelFitResult;
import dev.ufadevconf.application.PickupPointApplicationService;
import dev.ufadevconf.domain.PickupPoint;
import dev.ufadevconf.domain.Weight;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pickup-points")
@RequiredArgsConstructor
public class PickupPointController {

    private final PickupPointApplicationService applicationService;

    @PostMapping
    public PickupPointResponse create(@RequestBody CreatePickupPointRequest request) {
        var minWeight = Weight.ofKilograms(request.minWeightKg());
        var maxWeight = Weight.ofKilograms(request.maxWeightKg());
        var command = new CreatePickupPointCommand(
                request.code(),
                request.address(),
                minWeight.grams(),
                maxWeight.grams()
        );
        var pickupPoint = applicationService.registerPickupPoint(command);
        return toResponse(pickupPoint);
    }

    @PostMapping("/{code}/fit-check")
    public ParcelFitResponse checkParcelFits(@PathVariable String code, @RequestBody ParcelRequest request) {
        var query = new CheckParcelFitsQuery(
                code,
                request.trackingNumber(),
                request.contentsDescription(),
                request.parcelWeightGrams()
        );
        ParcelFitResult result = applicationService.checkParcelFits(query);
        return new ParcelFitResponse(result.pickupPointCode(), result.trackingNumber(), result.fits());
    }

    private static PickupPointResponse toResponse(PickupPoint pickupPoint) {
        return new PickupPointResponse(
                pickupPoint.getCode(),
                pickupPoint.getAddress(),
                pickupPoint.getMinWeight().grams(),
                pickupPoint.getMaxWeight().grams()
        );
    }

    public record CreatePickupPointRequest(String code, String address, double minWeightKg, double maxWeightKg) {
    }

    public record ParcelRequest(String trackingNumber, String contentsDescription, long parcelWeightGrams) {
    }

    public record PickupPointResponse(String code, String address, long minWeightGrams, long maxWeightGrams) {
    }

    public record ParcelFitResponse(String pickupPointCode, String trackingNumber, boolean fits) {
    }
}
