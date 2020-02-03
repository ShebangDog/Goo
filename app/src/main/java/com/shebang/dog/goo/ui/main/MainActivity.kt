package com.shebang.dog.goo.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.ActivityMainBinding
import com.shebang.dog.goo.ui.favorite.FavoriteFragment
import com.shebang.dog.goo.ui.street.RestaurantStreetFragment
import com.shebang.dog.goo.ui.tab.TabbedFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val fragmentList = listOf<TabbedFragment>(RestaurantStreetFragment(), FavoriteFragment())

        binding.apply {
            viewPager.apply {
                offscreenPageLimit = fragmentList.size
                adapter = object : FragmentPagerAdapter(
                    supportFragmentManager,
                    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                ) {
                    override fun getItem(position: Int): Fragment {
                        return fragmentList[position]
                    }

                    override fun getCount(): Int {
                        return fragmentList.size
                    }

                    override fun getPageTitle(position: Int): CharSequence? {
                        return fragmentList[position].getTabTitle()
                    }
                }
            }

            tabLayout.apply {
                setupWithViewPager(binding.viewPager)
                fragmentList.forEachIndexed { index, tabbedFragment ->
                    getTabAt(index)?.setIcon(tabbedFragment.getTabIconId())
                }
            }

        }
    }
}