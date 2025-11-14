from __future__ import annotations

from dataclasses import dataclass


@dataclass(frozen=True)
class CreatePickupPointCommand:
    code: str
    address: str
    min_weight_grams: int
    max_weight_grams: int


@dataclass(frozen=True)
class CheckParcelFitsQuery:
    pickup_point_code: str
    tracking_number: str
    contents_description: str
    parcel_weight_grams: int


@dataclass(frozen=True)
class ParcelFitResult:
    pickup_point_code: str
    tracking_number: str
    fits: bool
