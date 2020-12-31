package com.kdaydin.sixtcars.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.kdaydin.sixtcars.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context).load(url).error(R.drawable.ic_car_48dp).into(view)
    }
}

@BindingAdapter("progress")
fun setProgressPercent(view: LinearProgressIndicator, progress: Float?) {
    val value = (100*(progress?:0f)).toInt()
    view.progress = value
}