package dev.ufadevconf.application;

public class PickupPointNotFoundException extends RuntimeException {

    public PickupPointNotFoundException(String code) {
        super("Pickup point not found: %s".formatted(code));
    }
}
