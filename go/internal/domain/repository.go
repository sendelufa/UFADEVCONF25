package domain

// PickupPointRepository abstracts persistence operations for pickup points.
type PickupPointRepository interface {
    Save(p PickupPoint) error
    FindByCode(code string) (PickupPoint, bool, error)
}
