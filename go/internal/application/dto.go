package application

// CreatePickupPointCommand contains data required to register a pickup point.
type CreatePickupPointCommand struct {
    Code           string
    Address        string
    MinWeightGrams int64
    MaxWeightGrams int64
}

// CheckParcelFitsQuery describes the scenario of checking a parcel.
type CheckParcelFitsQuery struct {
    PickupPointCode    string
    TrackingNumber     string
    ContentsDescription string
    ParcelWeightGrams  int64
}

// ParcelFitResult is returned by the application service.
type ParcelFitResult struct {
    PickupPointCode string
    TrackingNumber  string
    Fits            bool
}
