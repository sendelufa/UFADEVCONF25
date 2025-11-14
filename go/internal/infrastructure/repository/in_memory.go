package repository

import (
    "sync"

    "dev.ufadevconf/vo-go/internal/domain"
)

// InMemoryPickupPointRepository stores pickup points in a concurrent map.
type InMemoryPickupPointRepository struct {
    mu      sync.RWMutex
    storage map[string]domain.PickupPoint
}

func NewInMemoryPickupPointRepository() *InMemoryPickupPointRepository {
    return &InMemoryPickupPointRepository{
        storage: make(map[string]domain.PickupPoint),
    }
}

func (r *InMemoryPickupPointRepository) Save(p domain.PickupPoint) error {
    r.mu.Lock()
    defer r.mu.Unlock()
    r.storage[p.Code()] = p
    return nil
}

func (r *InMemoryPickupPointRepository) FindByCode(code string) (domain.PickupPoint, bool, error) {
    r.mu.RLock()
    defer r.mu.RUnlock()
    p, ok := r.storage[code]
    return p, ok, nil
}
