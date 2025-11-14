import pytest

from vo_ufadevconf.domain.parcel import Parcel
from vo_ufadevconf.domain.pickup_point import PickupPoint
from vo_ufadevconf.domain.weight import Weight


def test_pickup_point_accepts_parcel_within_range():
    pickup_point = PickupPoint(
        code="SPB-101",
        address="Санкт-Петербург, Невский пр., 1",
        min_weight=Weight(1000),
        max_weight=Weight(5000),
    )

    light_parcel = Parcel("TRACK-1", Weight(1500), "Документы")
    heavy_parcel = Parcel("TRACK-2", Weight(7000), "Инструменты")

    assert pickup_point.can_accept(light_parcel)
    assert not pickup_point.can_accept(heavy_parcel)


def test_pickup_point_rejects_parcel_below_minimum():
    pickup_point = PickupPoint(
        code="SPB-102",
        address="Санкт-Петербург, Литейный пр., 5",
        min_weight=Weight(2000),
        max_weight=Weight(6000),
    )

    too_light_parcel = Parcel("TRACK-3", Weight(1500), "Образцы")

    assert not pickup_point.can_accept(too_light_parcel)


@pytest.mark.parametrize("min_grams,max_grams", [(5000, 1000)])
def test_pickup_point_rejects_invalid_range(min_grams: int, max_grams: int):
    with pytest.raises(ValueError):
        PickupPoint("SPB", "Адрес", Weight(min_grams), Weight(max_grams))
