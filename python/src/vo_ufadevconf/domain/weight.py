from __future__ import annotations

from dataclasses import dataclass
from decimal import Decimal, ROUND_HALF_UP


@dataclass(frozen=True, order=True, slots=True)
class Weight:
    """Value Object that stores weight in grams and exposes helpers for comparisons."""

    grams: int

    def __post_init__(self) -> None:
        if not isinstance(self.grams, int):
            raise TypeError(f"Weight must be an integer, got {type(self.grams)}")
        if self.grams < 0:
            raise ValueError(f"Weight cannot be negative: {self.grams} g")

    @staticmethod
    def from_kilograms(kilograms: float) -> "Weight":
        """Create Weight from kilograms using half-up rounding."""
        decimal_value = Decimal(str(kilograms)) * Decimal(1000)
        rounded = decimal_value.quantize(Decimal("1"), rounding=ROUND_HALF_UP)
        return Weight(int(rounded))

    def to_kilograms(self) -> float:
        """Return weight expressed in kilograms."""
        return self.grams / 1000.0

    def is_between(self, first: "Weight", second: "Weight") -> bool:
        lower = min(first.grams, second.grams)
        upper = max(first.grams, second.grams)
        return lower <= self.grams <= upper

    def __add__(self, other: "Weight") -> "Weight":
        if not isinstance(other, Weight):
            return NotImplemented
        return Weight(self.grams + other.grams)

    def __sub__(self, other: "Weight") -> "Weight":
        if not isinstance(other, Weight):
            return NotImplemented
        return Weight(self.grams - other.grams)
