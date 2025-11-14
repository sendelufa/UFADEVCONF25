package dev.ufadevconf.infrastructure.repository;

import dev.ufadevconf.domain.PickupPoint;
import dev.ufadevconf.domain.PickupPointRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPickupPointRepository implements PickupPointRepository {

    private final Map<String, PickupPoint> storage = new ConcurrentHashMap<>();

    @Override
    public void save(PickupPoint pickupPoint) {
        storage.put(pickupPoint.getCode(), pickupPoint);
    }

    @Override
    public Optional<PickupPoint> findByCode(String code) {
        return Optional.ofNullable(storage.get(code));
    }
}
