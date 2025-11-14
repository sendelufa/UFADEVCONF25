from __future__ import annotations

from dataclasses import dataclass

from .weight import Weight


@dataclass(frozen=True)
class Parcel:
    tracking_number: str
    weight: Weight
    contents_description: str
