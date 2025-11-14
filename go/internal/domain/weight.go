package domain

import (
    "fmt"
    "math"
)

// Weight is a Value Object that stores weight in grams and provides helpers for domain rules.
type Weight struct {
    grams int64
}

// NewWeight validates and returns a Weight expressed in grams.
func NewWeight(grams int64) (Weight, error) {
    if grams < 0 {
        return Weight{}, fmt.Errorf("weight cannot be negative: %d g", grams)
    }
    return Weight{grams: grams}, nil
}

// WeightFromKilograms converts kilograms to grams using half-up rounding.
func WeightFromKilograms(kilograms float64) (Weight, error) {
    grams := int64(math.Round(kilograms * 1000))
    return NewWeight(grams)
}

// Grams exposes the primitive representation.
func (w Weight) Grams() int64 {
    return w.grams
}

// IsBetween returns true when the weight is between the provided bounds (order agnostic).
func (w Weight) IsBetween(first, second Weight) bool {
    lower := min(first.grams, second.grams)
    upper := max(first.grams, second.grams)
    return w.grams >= lower && w.grams <= upper
}

func min(a, b int64) int64 {
    if a < b {
        return a
    }
    return b
}

func max(a, b int64) int64 {
    if a > b {
        return a
    }
    return b
}
