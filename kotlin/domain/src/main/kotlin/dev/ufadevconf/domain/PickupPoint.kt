package dev.ufadevconf.domain

data class PickupPoint(
    val code: String,
    val address: String,
    val minWeight: Weight,
    val maxWeight: Weight
) {
    init {
        require(minWeight <= maxWeight) { "Min weight cannot exceed max weight" }
    }

    fun canAccept(parcel: Parcel): Boolean = parcel.weight.isBetween(maxWeight, minWeight)
}
