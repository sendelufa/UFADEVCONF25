package dev.ufadevconf.application;

import dev.ufadevconf.domain.Parcel;
import dev.ufadevconf.domain.PickupPoint;
import dev.ufadevconf.domain.PickupPointRepository;
import dev.ufadevconf.domain.Weight;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class PickupPointApplicationService {

    private final PickupPointRepository pickupPointRepository;

    public PickupPoint registerPickupPoint(CreatePickupPointCommand command) {
        Objects.requireNonNull(command, "command");
        var pickupPoint = new PickupPoint(
                command.code(),
                command.address(),
                new Weight(command.minWeightGrams()),
                new Weight(command.maxWeightGrams())
        );
        pickupPointRepository.save(pickupPoint);
        return pickupPoint;
    }

    public ParcelFitResult checkParcelFits(CheckParcelFitsQuery query) {
        Objects.requireNonNull(query, "query");
        var pickupPoint = pickupPointRepository.findByCode(query.pickupPointCode())
                .orElseThrow(() -> new PickupPointNotFoundException(query.pickupPointCode()));
        var parcel = new Parcel(
                query.trackingNumber(),
                new Weight(query.parcelWeightGrams()),
                query.contentsDescription()
        );
        return new ParcelFitResult(
                pickupPoint.getCode(),
                parcel.trackingNumber(),
                pickupPoint.canAccept(parcel)
        );
    }
}
