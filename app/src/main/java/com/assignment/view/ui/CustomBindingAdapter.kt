package com.assignment.view.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.assignment.R
import com.bumptech.glide.Glide

object CustomBindingAdapter {

    @JvmStatic
    @BindingAdapter("bind:imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher)
            .into(view)
    }
}