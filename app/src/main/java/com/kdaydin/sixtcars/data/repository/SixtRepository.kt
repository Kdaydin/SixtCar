package com.kdaydin.sixtcars.data.repository

import com.kdaydin.sixtcars.data.entities.SixtCar
import com.kdaydin.sixtcars.data.remote.UseCaseResult

interface SixtRepository {
    suspend fun getCars(): UseCaseResult<List<SixtCar>>

}