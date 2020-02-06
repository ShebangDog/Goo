package com.shebang.dog.goo.ui.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import dagger.android.support.DaggerFragment

abstract class TabbedFragment(@LayoutRes private val layoutResId: Int) : DaggerFragment() {
    @DrawableRes
    abstract fun getTabIconId(): Int

    abstract fun getTabTitle(): String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }
}