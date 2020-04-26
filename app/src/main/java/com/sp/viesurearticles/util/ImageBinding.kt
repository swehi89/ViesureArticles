package com.sp.viesurearticles.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun setImage(imageView: ImageView, imageUrl: String?) = imageUrl.let {
    Glide.with(imageView.context).load(it).centerCrop().into(imageView)
}

@BindingAdapter("thumb")
fun setImageThumb(imageView: ImageView, imageUrl: String?) = imageUrl.let {
    Glide.with(imageView.context).load(it).thumbnail(0.4f)
        .centerCrop().into(imageView)
}

