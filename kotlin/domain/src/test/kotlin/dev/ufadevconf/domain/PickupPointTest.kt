package dev.ufadevconf.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PickupPointTest {

    @Test
    @DisplayName("Пункт выдачи принимает посылку если вес в допустимых границах")
    fun acceptsParcelWithinRange() {
        val pickupPoint = PickupPoint(
            code = "SPB-101",
            address = "Санкт-Петербург, Невский проспект, 1",
            minWeight = Weight(1000),
            maxWeight = Weight(5000)
        )

        val lightParcel = Parcel("TRACK-1", Weight(1500), "Документы")
        val heavyParcel = Parcel("TRACK-2", Weight(7000), "Инструменты")

        assertThat(pickupPoint.canAccept(lightParcel)).isTrue()
        assertThat(pickupPoint.canAccept(heavyParcel)).isFalse()
    }

    @Test
    @DisplayName("Пункт выдачи отклоняет посылку легче минимального порога")
    fun rejectsParcelBelowMinimum() {
        val pickupPoint = PickupPoint(
            code = "SPB-102",
            address = "Санкт-Петербург, Литейный пр., 5",
            minWeight = Weight(2000),
            maxWeight = Weight(6000)
        )

        val tooLightParcel = Parcel("TRACK-3", Weight(1500), "Образцы")

        assertThat(pickupPoint.canAccept(tooLightParcel)).isFalse()
    }
}
