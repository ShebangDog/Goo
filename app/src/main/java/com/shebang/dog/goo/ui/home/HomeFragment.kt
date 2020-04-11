package com.shebang.dog.goo.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentHomeBinding
import com.shebang.dog.goo.ui.favorite.FavoriteFragment
import com.shebang.dog.goo.ui.street.RestaurantStreetFragment

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val fragmentList = listOf({ RestaurantStreetFragment() }, { FavoriteFragment() })
        val fragmentListForTab = fragmentList.map { constructor ->
            constructor().let { FragmentDataOnTab(it.tabTitle, it.tabIconId) }
        }

        binding.apply {
            viewPager.apply {
                offscreenPageLimit = fragmentList.size
                adapter = object : FragmentStateAdapter(this@HomeFragment) {

                    override fun getItemCount(): Int {
                        return fragmentList.size
                    }

                    override fun createFragment(position: Int): Fragment {
                        return fragmentList[position].invoke()
                    }
                }
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setIcon(fragmentListForTab[position].iconId)
                tab.text = fragmentListForTab[position].title
            }.attach()
        }
    }

    data class FragmentDataOnTab(val title: String, val iconId: Int)
}