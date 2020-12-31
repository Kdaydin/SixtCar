package com.kdaydin.sixtcars.ui.listener

import com.kdaydin.sixtcars.data.entities.SixtCar

interface CarListButtonListener {
    fun onGetDirectionClicked(item: SixtCar)
    fun onGetInfoClicked(item: SixtCar)
}