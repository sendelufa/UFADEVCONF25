package dev.ufadevconf.application;

import java.util.Objects;

public record CreatePickupPointCommand(String code, String address, long minWeightGrams, long maxWeightGrams) {
    public CreatePickupPointCommand {
        Objects.requireNonNull(code, "code");
        Objects.requireNonNull(address, "address");
        if (minWeightGrams < 0) {
            throw new IllegalArgumentException("Minimum weight cannot be negative");
        }
        if (maxWeightGrams < 0) {
            throw new IllegalArgumentException("Maximum weight cannot be negative");
        }
    }
}
