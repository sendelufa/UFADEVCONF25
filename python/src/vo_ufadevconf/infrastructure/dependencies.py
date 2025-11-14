from __future__ import annotations

from ..application.service import PickupPointApplicationService
from .repository import InMemoryPickupPointRepository

_pickup_point_repository = InMemoryPickupPointRepository()
pickup_point_service = PickupPointApplicationService(_pickup_point_repository)
