package domain

import "fmt"

// PickupPoint models delivery constraints for a point of pickup.
type PickupPoint struct {
    code     string
    address  string
    minWeight Weight
    maxWeight Weight
}

// NewPickupPoint validates and creates a PickupPoint aggregate root.
func NewPickupPoint(code, address string, minWeight, maxWeight Weight) (PickupPoint, error) {
    if code == "" {
        return PickupPoint{}, fmt.Errorf("code cannot be empty")
    }
    if address == "" {
        return PickupPoint{}, fmt.Errorf("address cannot be empty")
    }
    if minWeight.Grams() > maxWeight.Grams() {
        return PickupPoint{}, fmt.Errorf("min weight cannot exceed max weight")
    }
    return PickupPoint{code: code, address: address, minWeight: minWeight, maxWeight: maxWeight}, nil
}

// Code returns pickup point identifier.
func (p PickupPoint) Code() string { return p.code }

// Address returns pickup point address.
func (p PickupPoint) Address() string { return p.address }

// MinWeight exposes the lower weight bound.
func (p PickupPoint) MinWeight() Weight { return p.minWeight }

// MaxWeight exposes the upper weight bound.
func (p PickupPoint) MaxWeight() Weight { return p.maxWeight }

// CanAccept checks whether a parcel fits weight constraints.
func (p PickupPoint) CanAccept(parcel Parcel) bool {
    return parcel.Weight.IsBetween(p.maxWeight, p.minWeight)
}
