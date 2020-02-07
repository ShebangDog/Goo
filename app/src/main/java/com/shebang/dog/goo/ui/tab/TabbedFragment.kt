package com.shebang.dog.goo.ui.tab

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

abstract class TabbedFragment(@LayoutRes private val layoutResId: Int) :
    MyDaggerFragment(layoutResId) {

    @DrawableRes
    abstract fun getTabIconId(): Int

    abstract fun getTabTitle(): String
}
