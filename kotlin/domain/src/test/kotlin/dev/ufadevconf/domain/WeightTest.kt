package dev.ufadevconf.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class WeightTest {

    @Test
    @DisplayName("Вес не может быть отрицательным")
    fun negativeWeightIsRejected() {
        assertThatThrownBy { Weight(-1) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Weight cannot be negative")
    }

    @Test
    @DisplayName("Перевод килограммов в граммы использует банковское округление")
    fun convertsKilogramsToGramsUsingHalfUpRounding() {
        val weight = Weight.ofKilograms(0.3335)
        assertThat(weight.grams).isEqualTo(334)
    }

    @Test
    @DisplayName("Принадлежность диапазону не зависит от порядка границ")
    fun belongsToRangeRegardlessOfBoundOrder() {
        val min = Weight(3000)
        val max = Weight(5000)
        val current = Weight(4000)

        assertThat(current.isBetween(max, min)).isTrue()
        assertThat(current.isBetween(min, max)).isTrue()
        assertThat(Weight(1000).isBetween(min, max)).isFalse()
    }

    @ParameterizedTest
    @DisplayName("Вес создаётся из корректных значений в граммах")
    @ValueSource(longs = [0L, 1L, 2500L, 100_000L])
    fun createsFromValidGramValues(grams: Long) {
        assertThat(Weight(grams).grams).isEqualTo(grams)
    }

    @ParameterizedTest
    @DisplayName("Отрицательные значения грамм приводят к исключению")
    @ValueSource(longs = [-1L, -10L, -1000L, Long.MIN_VALUE])
    fun negativeGramValuesAreRejected(grams: Long) {
        assertThatThrownBy { Weight(grams) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Weight cannot be negative")
    }

    @Test
    @DisplayName("Равенство весов зависит только от граммов")
    fun equalityDependsOnlyOnGrams() {
        val first = Weight(1500)
        val second = Weight(1500)
        val third = Weight(1600)

        assertThat(first).isEqualTo(second)
        assertThat(first.hashCode()).isEqualTo(second.hashCode())
        assertThat(first).isNotEqualTo(third)
    }
}
