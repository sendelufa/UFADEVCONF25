package dev.ufadevconf.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object that models parcel weight in grams and exposes convenience helpers for domain logic.
 */
public record Weight(long grams) implements Comparable<Weight> {

    public Weight {
        if (grams < 0) {
            throw new IllegalArgumentException("Weight cannot be negative: %s g".formatted(grams));
        }
    }

    public static Weight ofKilograms(double kilograms) {
        var inGrams = BigDecimal.valueOf(kilograms)
                .multiply(BigDecimal.valueOf(1000));
        var rounded = inGrams.setScale(0, RoundingMode.HALF_UP);
        return new Weight(rounded.longValueExact());
    }

    public boolean isBetween(Weight first, Weight second) {
        var lowerBound = Math.min(first.grams, second.grams);
        var upperBound = Math.max(first.grams, second.grams);
        return grams >= lowerBound && grams <= upperBound;
    }

    @Override
    public int compareTo(Weight other) {
        return Long.compare(this.grams, other.grams);
    }
}
