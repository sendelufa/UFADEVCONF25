package application

import (
    "errors"
    "fmt"

    "dev.ufadevconf/vo-go/internal/domain"
)

var ErrPickupPointNotFound = errors.New("pickup point not found")

// PickupPointApplicationService orchestrates domain use cases.
type PickupPointApplicationService struct {
    repo domain.PickupPointRepository
}

func NewPickupPointApplicationService(repo domain.PickupPointRepository) *PickupPointApplicationService {
    return &PickupPointApplicationService{repo: repo}
}

func (s *PickupPointApplicationService) RegisterPickupPoint(cmd CreatePickupPointCommand) (domain.PickupPoint, error) {
    minWeight, err := domain.NewWeight(cmd.MinWeightGrams)
    if err != nil {
        return domain.PickupPoint{}, err
    }
    maxWeight, err := domain.NewWeight(cmd.MaxWeightGrams)
    if err != nil {
        return domain.PickupPoint{}, err
    }
    pickupPoint, err := domain.NewPickupPoint(cmd.Code, cmd.Address, minWeight, maxWeight)
    if err != nil {
        return domain.PickupPoint{}, err
    }
    if err := s.repo.Save(pickupPoint); err != nil {
        return domain.PickupPoint{}, err
    }
    return pickupPoint, nil
}

func (s *PickupPointApplicationService) CheckParcelFits(query CheckParcelFitsQuery) (ParcelFitResult, error) {
    pickupPoint, found, err := s.repo.FindByCode(query.PickupPointCode)
    if err != nil {
        return ParcelFitResult{}, err
    }
    if !found {
        return ParcelFitResult{}, fmt.Errorf("%w: %s", ErrPickupPointNotFound, query.PickupPointCode)
    }
    parcelWeight, err := domain.NewWeight(query.ParcelWeightGrams)
    if err != nil {
        return ParcelFitResult{}, err
    }
    parcel := domain.Parcel{
        TrackingNumber:     query.TrackingNumber,
        Weight:             parcelWeight,
        ContentsDescription: query.ContentsDescription,
    }
    fits := pickupPoint.CanAccept(parcel)
    return ParcelFitResult{
        PickupPointCode: pickupPoint.Code(),
        TrackingNumber:  parcel.TrackingNumber,
        Fits:            fits,
    }, nil
}
