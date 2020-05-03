package com.shebang.dog.goo.ui.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("android:visibility")
fun bindVisibility(view: ViewPager2, isVisible: Boolean) {
    view.visibility = booleanToVisibility(isVisible)
}

@BindingAdapter("android:visibility")
fun bindVisibility(
    view: com.tbuonomo.viewpagerdotsindicator.DotsIndicator,
    isVisible: Boolean
) {

    view.visibility = booleanToVisibility(isVisible)
}

@BindingAdapter("android:visibility")
fun bindVisibility(view: TextView, isVisible: Boolean) {
    view.visibility = booleanToVisibility(isVisible)
}

private fun booleanToVisibility(isVisible: Boolean): Int {
    return if (isVisible) View.VISIBLE else View.GONE
}