package dev.ufadevconf.application

data class CreatePickupPointCommand(
    val code: String,
    val address: String,
    val minWeightGrams: Long,
    val maxWeightGrams: Long
) {
    init {
        require(minWeightGrams >= 0) { "Minimum weight cannot be negative" }
        require(maxWeightGrams >= 0) { "Maximum weight cannot be negative" }
    }
}
