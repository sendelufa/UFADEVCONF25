package dev.ufadevconf.domain;

import java.util.Optional;

public interface PickupPointRepository {

    void save(PickupPoint pickupPoint);

    Optional<PickupPoint> findByCode(String code);
}
