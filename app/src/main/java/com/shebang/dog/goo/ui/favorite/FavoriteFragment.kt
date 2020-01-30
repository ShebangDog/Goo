package com.shebang.dog.goo.ui.favorite

import com.shebang.dog.goo.R
import com.shebang.dog.goo.ui.tab.TabbedFragment

class FavoriteFragment : TabbedFragment(R.layout.fragment_favorite_list) {
    override fun getTabIconId(): Int {
        return R.drawable.ic_favorite_pink_24dp
    }

    override fun getTabTitle(): String {
        return "FAVORITE"
    }
}