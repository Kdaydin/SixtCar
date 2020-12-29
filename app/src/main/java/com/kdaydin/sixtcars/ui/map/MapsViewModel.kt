package com.kdaydin.sixtcars.ui.map

import android.util.Log
import android.widget.SimpleCursorTreeAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kdaydin.sixtcars.data.entities.SixtCar
import com.kdaydin.sixtcars.data.remote.UseCaseResult
import com.kdaydin.sixtcars.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MapsViewModel : BaseViewModel() {
    val carData = MutableLiveData<List<SixtCar>?>(null)
    val TAG = "NETWORK"
    fun getCars(){
        viewModelScope.launch {
            when(val result = sixtRepository.getCars()){
                is UseCaseResult.Success -> {
                    carData.postValue(result.data)
                    Log.d(TAG, "getCars: ${result.data}")
                }
            }
        }
    }
}