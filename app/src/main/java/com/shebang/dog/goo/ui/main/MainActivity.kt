package com.shebang.dog.goo.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
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

        val fragmentList = listOf(RestaurantStreetFragment(), FavoriteFragment())

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
                        return fragmentList[position].tabTitle
                    }
                }
            }

            tabLayout.apply {
                setupWithViewPager(binding.viewPager)
                fragmentList.forEachIndexed { index, tabbedFragment ->
                    getTabAt(index)?.setIcon(tabbedFragment.tabIconId)
                }
            }
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
}