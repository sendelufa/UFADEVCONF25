from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException, status
from pydantic import BaseModel, Field

from ..application.dto import CheckParcelFitsQuery, CreatePickupPointCommand
from ..application.exceptions import PickupPointNotFoundError
from ..application.service import PickupPointApplicationService
from ..domain.weight import Weight


class CreatePickupPointRequest(BaseModel):
    code: str
    address: str
    min_weight_kg: float = Field(alias="minWeightKg")
    max_weight_kg: float = Field(alias="maxWeightKg")

    class Config:
        populate_by_name = True
        json_schema_extra = {
            "example": {
                "code": "SPB-101",
                "address": "Санкт-Петербург, Невский проспект, 1",
                "minWeightKg": 1.0,
                "maxWeightKg": 5.0,
            }
        }


class ParcelRequest(BaseModel):
    tracking_number: str = Field(alias="trackingNumber")
    contents_description: str = Field(alias="contentsDescription")
    parcel_weight_grams: int = Field(alias="parcelWeightGrams")

    class Config:
        populate_by_name = True
        json_schema_extra = {
            "example": {
                "trackingNumber": "TRACK-1",
                "contentsDescription": "Документы",
                "parcelWeightGrams": 2300,
            }
        }


class PickupPointResponse(BaseModel):
    code: str
    address: str
    min_weight_grams: int
    max_weight_grams: int


class ParcelFitResponse(BaseModel):
    pickup_point_code: str
    tracking_number: str
    fits: bool


router = APIRouter(prefix="/api/pickup-points", tags=["pickup-points"])


def get_service() -> PickupPointApplicationService:
    from .dependencies import pickup_point_service

    return pickup_point_service


@router.post("", response_model=PickupPointResponse, status_code=status.HTTP_201_CREATED)
async def create_pickup_point(
    request: CreatePickupPointRequest,
    service: PickupPointApplicationService = Depends(get_service),
) -> PickupPointResponse:
    try:
        min_weight = Weight.from_kilograms(request.min_weight_kg)
        max_weight = Weight.from_kilograms(request.max_weight_kg)
    except ValueError as exc:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(exc)) from exc

    command = CreatePickupPointCommand(
        code=request.code,
        address=request.address,
        min_weight_grams=min_weight.grams,
        max_weight_grams=max_weight.grams,
    )
    try:
        pickup_point = service.register_pickup_point(command)
    except ValueError as exc:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(exc)) from exc
    return PickupPointResponse(
        code=pickup_point.code,
        address=pickup_point.address,
        min_weight_grams=pickup_point.min_weight.grams,
        max_weight_grams=pickup_point.max_weight.grams,
    )


@router.post("/{code}/fit-check", response_model=ParcelFitResponse)
async def check_parcel_fits(
    code: str,
    request: ParcelRequest,
    service: PickupPointApplicationService = Depends(get_service),
) -> ParcelFitResponse:
    query = CheckParcelFitsQuery(
        pickup_point_code=code,
        tracking_number=request.tracking_number,
        contents_description=request.contents_description,
        parcel_weight_grams=request.parcel_weight_grams,
    )
    try:
        result = service.check_parcel_fits(query)
    except PickupPointNotFoundError as exc:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=str(exc)) from exc
    except ValueError as exc:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail=str(exc)) from exc
    return ParcelFitResponse(
        pickup_point_code=result.pickup_point_code,
        tracking_number=result.tracking_number,
        fits=result.fits,
    )
