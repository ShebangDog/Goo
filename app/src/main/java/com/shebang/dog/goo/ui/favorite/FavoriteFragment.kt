package com.shebang.dog.goo.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentFavoriteListBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ext.assistedViewModels
import com.shebang.dog.goo.ui.tab.TabbedFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteFragment : TabbedFragment(R.layout.fragment_favorite_list) {
    @Inject
    lateinit var favoriteViewModelFactory: ViewModelFactory
    private val favoriteViewModel by assistedViewModels {
        favoriteViewModelFactory.create(FavoriteViewModel::class.java)
    }

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var binding: FragmentFavoriteListBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteListBinding.bind(view)

        favoriteViewModel.restaurantStreet.observe(this) {
            favoriteAdapter.restaurantStreet = it
        }

        binding.favoriteListRecycerView.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(view.context)
        }
    }

    override fun onResume() {
        super.onResume()

        favoriteViewModel.walkFavoriteRestaurantStreet()
    }

    override fun getTabIconId(): Int {
        return R.drawable.ic_favorite_pink_24dp
    }

    override fun getTabTitle(): String {
        return "FAVORITE"
    }
}