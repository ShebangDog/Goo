package com.shebang.dog.goo.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentDataList = HomeFragmentPagerAdapter.FragmentCreatorList.map {
            it.create().let { fragment ->
                FragmentDataOnTab(fragment.tabTitle, fragment.tabIconId)
            }
        }

        binding.apply {
            viewPager.adapter = HomeFragmentPagerAdapter(this@HomeFragment)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setIcon(fragmentDataList[position].iconId)
                tab.text = fragmentDataList[position].title
            }.attach()
        }
    }

    data class FragmentDataOnTab(val title: String, val iconId: Int)
}