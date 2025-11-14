package dev.ufadevconf.domain

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Value Object that stores weight in grams and exposes helpers for domain invariants.
 */
@JvmInline
value class Weight(val grams: Long) : Comparable<Weight> {

    init {
        require(grams >= 0) { "Weight cannot be negative: $grams g" }
    }

    fun isBetween(first: Weight, second: Weight): Boolean {
        val lower = minOf(first.grams, second.grams)
        val upper = maxOf(first.grams, second.grams)
        return grams in lower..upper
    }

    override fun compareTo(other: Weight): Int = grams.compareTo(other.grams)

    companion object {
        fun ofKilograms(kilograms: Double): Weight {
            val inGrams = BigDecimal.valueOf(kilograms).multiply(BigDecimal.valueOf(1000))
            val rounded = inGrams.setScale(0, RoundingMode.HALF_UP)
            return Weight(rounded.longValueExact())
        }
    }
}
