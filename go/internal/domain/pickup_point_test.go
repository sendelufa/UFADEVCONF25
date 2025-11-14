package domain

import "testing"

func TestPickupPointAcceptsParcelWithinRange(t *testing.T) {
    min, _ := NewWeight(1000)
    max, _ := NewWeight(5000)
    pickupPoint, err := NewPickupPoint("SPB-101", "Санкт-Петербург, Невский пр., 1", min, max)
    if err != nil {
        t.Fatalf("unexpected error: %v", err)
    }
    lightParcel := Parcel{TrackingNumber: "TRACK-1", Weight: min}
    heavyWeight, _ := NewWeight(7000)
    heavyParcel := Parcel{TrackingNumber: "TRACK-2", Weight: heavyWeight}

    if !pickupPoint.CanAccept(lightParcel) {
        t.Fatalf("expected light parcel to be accepted")
    }
    if pickupPoint.CanAccept(heavyParcel) {
        t.Fatalf("expected heavy parcel to be rejected")
    }
}

func TestPickupPointRejectsParcelBelowMinimum(t *testing.T) {
    min, _ := NewWeight(2000)
    max, _ := NewWeight(6000)
    pickupPoint, err := NewPickupPoint("SPB-102", "Санкт-Петербург, Литейный пр., 5", min, max)
    if err != nil {
        t.Fatalf("unexpected error: %v", err)
    }
    tooLight, _ := NewWeight(1500)
    parcel := Parcel{TrackingNumber: "TRACK-3", Weight: tooLight}

    if pickupPoint.CanAccept(parcel) {
        t.Fatalf("expected parcel below minimum to be rejected")
    }
}
