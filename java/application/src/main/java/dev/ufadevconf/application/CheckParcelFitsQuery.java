package dev.ufadevconf.application;

public record CheckParcelFitsQuery(String pickupPointCode,
                                   String trackingNumber,
                                   String contentsDescription,
                                   long parcelWeightGrams) {
}
