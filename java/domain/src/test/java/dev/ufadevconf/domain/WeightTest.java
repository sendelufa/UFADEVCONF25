package dev.ufadevconf.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WeightTest {

    @Test
    @DisplayName("Вес не может быть отрицательным")
    void negativeWeightIsRejected() {
        assertThatThrownBy(() -> new Weight(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Weight cannot be negative");
    }

    @Test
    @DisplayName("Перевод килограммов в граммы использует банковское округление")
    void convertsKilogramsToGramsUsingHalfUpRounding() {
        var weight = Weight.ofKilograms(0.3335);
        assertThat(weight.grams()).isEqualTo(334);
    }

    @Test
    @DisplayName("Принадлежность диапазону не зависит от порядка границ")
    void belongsToRangeRegardlessOfBoundOrder() {
        var min = new Weight(3000);
        var max = new Weight(5000);
        var current = new Weight(4000);

        assertThat(current.isBetween(max, min)).isTrue();
        assertThat(current.isBetween(min, max)).isTrue();
        assertThat(new Weight(1000).isBetween(min, max)).isFalse();
    }

    @ParameterizedTest
    @DisplayName("Вес создаётся из корректных значений в граммах")
    @ValueSource(longs = {0L, 1L, 2500L, 100_000L})
    void createsFromValidGramValues(long grams) {
        assertThat(new Weight(grams).grams()).isEqualTo(grams);
    }

    @ParameterizedTest
    @DisplayName("Отрицательные значения грамм приводят к исключению")
    @ValueSource(longs = {-1L, -10L, -1000L, Long.MIN_VALUE})
    void negativeGramValuesAreRejected(long grams) {
        assertThatThrownBy(() -> new Weight(grams))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Weight cannot be negative");
    }

    @Test
    @DisplayName("Равенство весов зависит только от граммов")
    void equalityDependsOnlyOnGrams() {
        var first = new Weight(1500);
        var second = new Weight(1500);
        var third = new Weight(1600);

        assertThat(first).isEqualTo(second);
        assertThat(first).hasSameHashCodeAs(second);
        assertThat(first).isNotEqualTo(third);
    }
}
