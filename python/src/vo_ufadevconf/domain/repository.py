from __future__ import annotations

from typing import Protocol

from .pickup_point import PickupPoint


class PickupPointRepository(Protocol):
    def save(self, pickup_point: PickupPoint) -> None:
        ...

    def find_by_code(self, code: str) -> PickupPoint | None:
        ...
