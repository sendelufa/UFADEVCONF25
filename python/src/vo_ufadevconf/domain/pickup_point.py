from __future__ import annotations

from dataclasses import dataclass

from .parcel import Parcel
from .weight import Weight


@dataclass(frozen=True)
class PickupPoint:
    code: str
    address: str
    min_weight: Weight
    max_weight: Weight

    def __post_init__(self) -> None:
        if not self.code:
            raise ValueError("Pickup point code cannot be empty")
        if not self.address:
            raise ValueError("Pickup point address cannot be empty")
        if self.min_weight > self.max_weight:
            raise ValueError("Min weight cannot exceed max weight")

    def can_accept(self, parcel: Parcel) -> bool:
        return parcel.weight.is_between(self.min_weight, self.max_weight)
