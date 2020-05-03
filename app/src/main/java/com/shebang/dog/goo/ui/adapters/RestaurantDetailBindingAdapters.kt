package com.shebang.dog.goo.ui.adapters

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("android:visibility")
fun bindVisibility(view: ViewPager2, imageUrlList: List<String>?) {
    view.visibility = if (imageUrlList.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("android:visibility")
fun bindVisibility(
    view: com.tbuonomo.viewpagerdotsindicator.DotsIndicator,
    imageUrlList: List<String>?
) {

    val size = imageUrlList?.size ?: 0
    view.visibility = if (size < 2) View.GONE else View.VISIBLE
}