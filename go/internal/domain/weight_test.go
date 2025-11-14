package domain

import "testing"

func TestNewWeightRejectsNegativeValues(t *testing.T) {
    if _, err := NewWeight(-1); err == nil {
        t.Fatalf("expected error for negative weight")
    }
}

func TestWeightFromKilogramsUsesHalfUpRounding(t *testing.T) {
    weight, err := WeightFromKilograms(0.3335)
    if err != nil {
        t.Fatalf("unexpected error: %v", err)
    }
    if weight.Grams() != 334 {
        t.Fatalf("expected 334 grams, got %d", weight.Grams())
    }
}

func TestWeightBelongsToRangeRegardlessOfBoundOrder(t *testing.T) {
    min, _ := NewWeight(3000)
    max, _ := NewWeight(5000)
    current, _ := NewWeight(4000)

    if !current.IsBetween(max, min) {
        t.Fatalf("expected weight to be within reversed bounds")
    }
    if !current.IsBetween(min, max) {
        t.Fatalf("expected weight to be within bounds")
    }
    tooLight, _ := NewWeight(1000)
    if tooLight.IsBetween(min, max) {
        t.Fatalf("expected weight outside bounds")
    }
}

func TestWeightEqualityDependsOnGrams(t *testing.T) {
    first, _ := NewWeight(1500)
    second, _ := NewWeight(1500)
    third, _ := NewWeight(1600)

    if first != second {
        t.Fatalf("weights with equal grams should be equal")
    }
    if first == third {
        t.Fatalf("weights with different grams should differ")
    }
}
