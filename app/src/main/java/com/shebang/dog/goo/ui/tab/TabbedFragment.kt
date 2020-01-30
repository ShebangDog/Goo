package com.shebang.dog.goo.ui.tab

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class TabbedFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    @DrawableRes
    abstract fun getTabIconId(): Int

    abstract fun getTabTitle(): String
}