import pytest

from vo_ufadevconf.domain.weight import Weight


def test_negative_weight_is_rejected():
    with pytest.raises(ValueError):
        Weight(-1)


def test_converts_kilograms_to_grams_using_half_up_rounding():
    weight = Weight.from_kilograms(0.3335)
    assert weight.grams == 334


def test_converts_weight_to_kilograms():
    weight = Weight(2500)
    assert weight.to_kilograms() == 2.5


def test_belongs_to_range_regardless_of_bound_order():
    min_weight = Weight(3000)
    max_weight = Weight(5000)
    current = Weight(4000)

    assert current.is_between(max_weight, min_weight)
    assert current.is_between(min_weight, max_weight)
    assert Weight(1000).is_between(min_weight, max_weight) is False


@pytest.mark.parametrize("grams", [0, 1, 2500, 100_000])
def test_creates_from_valid_gram_values(grams: int):
    assert Weight(grams).grams == grams


@pytest.mark.parametrize("grams", [-1, -10, -1000])
def test_negative_gram_values_are_rejected(grams: int):
    with pytest.raises(ValueError):
        Weight(grams)


def test_non_integer_weight_raises_type_error():
    with pytest.raises(TypeError):
        Weight(1.5)  # type: ignore[arg-type]


def test_equality_depends_only_on_grams():
    first = Weight(1500)
    second = Weight(1500)
    third = Weight(1600)

    assert first == second
    assert hash(first) == hash(second)
    assert first != third


def test_weight_arithmetic_operations():
    first = Weight(1500)
    second = Weight(500)

    assert (first + second).grams == 2000
    assert (first - second).grams == 1000


def test_weight_subtraction_with_negative_result_raises():
    light = Weight(500)
    heavy = Weight(2000)

    with pytest.raises(ValueError):
        _ = light - heavy
