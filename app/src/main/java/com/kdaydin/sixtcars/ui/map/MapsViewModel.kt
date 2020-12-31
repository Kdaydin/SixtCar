package com.kdaydin.sixtcars.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kdaydin.sixtcars.data.entities.SixtCar
import com.kdaydin.sixtcars.data.remote.UseCaseResult
import com.kdaydin.sixtcars.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MapsViewModel : BaseViewModel() {
    val carData = MutableLiveData<List<SixtCar>?>(null)
    val markerOptions = MutableLiveData<MutableList<MarkerOptions>>(mutableListOf())
    val markers = MutableLiveData<MutableList<Marker>>(mutableListOf())
    val TAG = "NETWORK"
    fun getCars() {
        viewModelScope.launch {
            when (val result = sixtRepository.getCars()) {
                is UseCaseResult.Success -> {
                    val sortedList = result.data.sortedWith(
                        compareBy({ it.latitude },
                            { it.longitude })
                    )
                    val markers = mutableListOf<MarkerOptions>()
                    sortedList.forEach { car ->
                        markers.add(
                            MarkerOptions()
                                .title(car.name)
                                .snippet("${car.modelName} ${car.transmission?.transmission}")
                                .position(
                                    LatLng(
                                        car.latitude?.toDouble() ?: 0.0,
                                        car.longitude?.toDouble() ?: 0.0
                                    )
                                )
                        )
                    }
                    carData.postValue(sortedList)
                    markerOptions.postValue(markers)
                }
            }
        }
    }
}