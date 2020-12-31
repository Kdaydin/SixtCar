package com.kdaydin.sixtcars.data.entities

data class SixtCar(
    val id: String? = "",
    val modelIdentifier: String? = "",
    val modelName: String? = "",
    val name: String? = "",
    val make: String? = "",
    val group: String? = "",
    val color: String? = "",
    val series: String? = "",
    val fuelType: FuelType? = FuelType.NA,
    val fuelLevel: Float? = 0f,
    val transmission: Transmission? = Transmission.NA,
    val licensePlate: String? = "",
    val latitude: Float? = 0f,
    val longitude: Float? = 0f,
    val innerCleanliness: String? = "",
    val carImageUrl: String? = "",
) {

}
