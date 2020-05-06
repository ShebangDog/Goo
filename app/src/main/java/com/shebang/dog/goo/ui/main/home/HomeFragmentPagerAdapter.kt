package com.shebang.dog.goo.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shebang.dog.goo.ui.base.TabbedFragment
import com.shebang.dog.goo.ui.main.favorite.FavoriteFragment
import com.shebang.dog.goo.ui.main.street.RestaurantStreetFragment

class HomeFragmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    class Creator(private val constructor: () -> TabbedFragment) {
        fun create(): TabbedFragment {
            return constructor.invoke()
        }
    }

    companion object {
        val FragmentCreatorList = listOf(
            Creator { RestaurantStreetFragment() },
            Creator { FavoriteFragment() }
        )
    }

    override fun getItemCount(): Int {
        return FragmentCreatorList.size
    }

    override fun createFragment(position: Int): Fragment {
        return FragmentCreatorList[position].create()
    }
}