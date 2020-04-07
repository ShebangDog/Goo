package com.shebang.dog.goo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.ActivityMainBinding
import com.shebang.dog.goo.ui.about.AboutActivity
import com.shebang.dog.goo.ui.favorite.FavoriteFragment
import com.shebang.dog.goo.ui.street.RestaurantStreetFragment
import com.shebang.dog.goo.util.PermissionGranter

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolBar)

        val fragmentList = listOf({ RestaurantStreetFragment() }, { FavoriteFragment() })
        val fragmentListForTab = fragmentList.map { constructor ->
            constructor().let { FragmentDataOnTab(it.tabTitle, it.tabIconId) }
        }

        binding.apply {
            viewPager.apply {
                offscreenPageLimit = fragmentList.size
                adapter = object : FragmentStateAdapter(
                    supportFragmentManager, lifecycle
                ) {
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

    override fun onStart() {
        super.onStart()

        PermissionGranter.requestPermission(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                showAboutScreen()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutScreen() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    data class FragmentDataOnTab(val title: String, val iconId: Int)
}