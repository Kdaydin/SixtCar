package com.kdaydin.sixtcars.ui.car_detail

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kdaydin.sixtcars.R
import com.kdaydin.sixtcars.data.entities.SixtCar
import com.kdaydin.sixtcars.databinding.BottomsheetCarDetailBinding

class CarDetailBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: BottomsheetCarDetailBinding
    var viewModel = SixtCar()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottomsheet_car_detail, container, false)
        binding.sixtCar = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppModalStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0x7f000000))
        return dialog
    }


    companion object {
        fun create(viewModel: SixtCar): CarDetailBottomSheet {
            val carDetailBottomSheet =
                CarDetailBottomSheet()
            carDetailBottomSheet.viewModel = viewModel
            return carDetailBottomSheet

        }

    }

}