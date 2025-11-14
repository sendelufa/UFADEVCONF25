package domain

// Parcel describes a shipment.
type Parcel struct {
    TrackingNumber     string
    Weight             Weight
    ContentsDescription string
}
