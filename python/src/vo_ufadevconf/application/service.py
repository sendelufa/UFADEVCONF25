from __future__ import annotations

from .dto import CheckParcelFitsQuery, CreatePickupPointCommand, ParcelFitResult
from .exceptions import PickupPointNotFoundError
from ..domain.parcel import Parcel
from ..domain.pickup_point import PickupPoint
from ..domain.repository import PickupPointRepository
from ..domain.weight import Weight


class PickupPointApplicationService:
    def __init__(self, repository: PickupPointRepository) -> None:
        self._repository = repository

    def register_pickup_point(self, command: CreatePickupPointCommand) -> PickupPoint:
        min_weight = Weight(command.min_weight_grams)
        max_weight = Weight(command.max_weight_grams)
        pickup_point = PickupPoint(
            code=command.code,
            address=command.address,
            min_weight=min_weight,
            max_weight=max_weight,
        )
        self._repository.save(pickup_point)
        return pickup_point

    def check_parcel_fits(self, query: CheckParcelFitsQuery) -> ParcelFitResult:
        pickup_point = self._repository.find_by_code(query.pickup_point_code)
        if pickup_point is None:
            raise PickupPointNotFoundError(query.pickup_point_code)
        parcel = Parcel(
            tracking_number=query.tracking_number,
            weight=Weight(query.parcel_weight_grams),
            contents_description=query.contents_description,
        )
        fits = pickup_point.can_accept(parcel)
        return ParcelFitResult(
            pickup_point_code=pickup_point.code,
            tracking_number=parcel.tracking_number,
            fits=fits,
        )
