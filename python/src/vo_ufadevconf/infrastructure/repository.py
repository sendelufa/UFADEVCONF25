from __future__ import annotations

from threading import RLock

from ..domain.pickup_point import PickupPoint
from ..domain.repository import PickupPointRepository


class InMemoryPickupPointRepository(PickupPointRepository):
    def __init__(self) -> None:
        self._lock = RLock()
        self._storage: dict[str, PickupPoint] = {}

    def save(self, pickup_point: PickupPoint) -> None:
        with self._lock:
            self._storage[pickup_point.code] = pickup_point

    def find_by_code(self, code: str) -> PickupPoint | None:
        with self._lock:
            return self._storage.get(code)
