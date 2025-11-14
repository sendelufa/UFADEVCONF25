package dev.ufadevconf.domain;

import lombok.NonNull;

public record Parcel(
        @NonNull String trackingNumber,
        @NonNull Weight weight,
        @NonNull String contentsDescription) {
}
