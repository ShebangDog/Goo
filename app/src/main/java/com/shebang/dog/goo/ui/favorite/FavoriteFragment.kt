package com.shebang.dog.goo.ui.favorite

import android.content.Context
import com.shebang.dog.goo.R
import com.shebang.dog.goo.ui.street.RestaurantStreetAdapter
import com.shebang.dog.goo.ui.street.RestaurantStreetViewModel
import com.shebang.dog.goo.ui.tab.TabbedFragment
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteFragment : TabbedFragment(R.layout.fragment_favorite_list), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: AndroidInjector<Any>

    @Inject
    lateinit var restaurantStreetViewModel: RestaurantStreetViewModel

    @Inject
    lateinit var restaurantStreetAdapter: RestaurantStreetAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun getTabIconId(): Int {
        return R.drawable.ic_favorite_pink_24dp
    }

    override fun getTabTitle(): String {
        return "FAVORITE"
    }
}