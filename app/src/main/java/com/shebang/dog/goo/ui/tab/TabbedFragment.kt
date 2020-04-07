package com.shebang.dog.goo.ui.tab

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

abstract class TabbedFragment(@LayoutRes private val layoutResId: Int) :
    MyDaggerFragment(layoutResId) {

    abstract val tabIconId: Int
        @DrawableRes
        get

    abstract val tabTitle: String
}
