package dev.ufadevconf.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PickupPointTest {

    @Test
    @DisplayName("Пункт выдачи принимает посылку если вес в допустимых границах")
    void acceptsParcelWithinRange() {
        var pickupPoint = new PickupPoint(
                "SPB-101",
                "Санкт-Петербург, Невский проспект, 1",
                new Weight(1000),
                new Weight(5000)
        );

        var lightParcel = new Parcel("TRACK-1", new Weight(1500), "Документы");
        var heavyParcel = new Parcel("TRACK-2", new Weight(7000), "Инструменты");

        assertThat(pickupPoint.canAccept(lightParcel)).isTrue();
        assertThat(pickupPoint.canAccept(heavyParcel)).isFalse();
    }

    @Test
    @DisplayName("Пункт выдачи отклоняет посылку легче минимального порога")
    void rejectsParcelBelowMinimum() {
        var pickupPoint = new PickupPoint(
                "SPB-102",
                "Санкт-Петербург, Литейный проспект, 5",
                new Weight(2000),
                new Weight(6000)
        );

        var tooLightParcel = new Parcel("TRACK-3", new Weight(1500), "Образцы");

        assertThat(pickupPoint.canAccept(tooLightParcel)).isFalse();
    }
}
