class PickupPointNotFoundError(Exception):
    def __init__(self, code: str) -> None:
        super().__init__(f"Pickup point not found: {code}")
