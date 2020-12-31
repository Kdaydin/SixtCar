package com.kdaydin.sixtcars.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.kdaydin.sixtcars.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context).load(url).error(R.drawable.ic_launcher_foreground).into(view)
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ShapeableImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context).load(url).error(R.drawable.ic_launcher_foreground).into(view)
    }
}

@BindingAdapter("progress")
fun setProgressPercent(view: LinearProgressIndicator, progress: Float?) {
    val value = (100 * (progress ?: 0f)).toInt()
    view.progress = value
}



@BindingAdapter("drawbleEnd")
fun setDrawableEnd(view: TextView, @DrawableRes resID: Int) {
    view.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, resID, 0)
}