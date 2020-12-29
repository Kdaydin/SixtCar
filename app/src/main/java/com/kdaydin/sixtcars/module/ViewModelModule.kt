package com.kdaydin.sixtcars.module

import com.kdaydin.sixtcars.ui.map.MapsViewModel
import com.kdaydin.sixtcars.ui.base.BaseViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BaseViewModel() }
    viewModel { MapsViewModel() }
}