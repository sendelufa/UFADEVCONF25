package dev.ufadevconf.domain;

import lombok.Data;
import lombok.NonNull;

@Data
public final class PickupPoint {
    private final String code;
    private final String address;
    private final Weight minWeight;
    private final Weight maxWeight;

    public PickupPoint(@NonNull String code,
                       @NonNull String address,
                       @NonNull Weight minWeight,
                       @NonNull Weight maxWeight) {
        this.code = code;
        this.address = address;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        if (this.minWeight.compareTo(this.maxWeight) > 0) {
            throw new IllegalArgumentException("Min weight cannot exceed max weight");
        }
    }

    public boolean canAccept(Parcel parcel) {
        return parcel.weight().isBetween(maxWeight, minWeight);
    }
}
