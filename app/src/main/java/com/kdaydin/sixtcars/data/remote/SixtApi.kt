package com.kdaydin.sixtcars.data.remote

import com.kdaydin.sixtcars.data.entities.SixtCar
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface SixtApi {
    @GET("cars")
    fun getCars(): Deferred<List<SixtCar>>
}