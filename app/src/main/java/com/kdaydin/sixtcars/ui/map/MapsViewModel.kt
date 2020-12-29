package com.kdaydin.sixtcars.ui.map

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kdaydin.sixtcars.data.remote.UseCaseResult
import com.kdaydin.sixtcars.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MapsViewModel : BaseViewModel() {
    val TAG = "NETWORK"
    fun getCars(){
        viewModelScope.launch {
            when(val result = sixtRepository.getCars()){
                is UseCaseResult.Success -> {
                    Log.d(TAG, "getCars: ${result.data}")
                }
            }
        }
    }
}