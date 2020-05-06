package com.shebang.dog.goo.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentFavoriteListBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ui.base.TabbedFragment
import com.shebang.dog.goo.ui.common.widget.RestaurantCardView
import com.shebang.dog.goo.ui.detail.RestaurantDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class FavoriteFragment : TabbedFragment(R.layout.fragment_favorite_list) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by viewModels<FavoriteViewModel> { viewModelFactory }
    private val sharedViewModel by activityViewModels<RestaurantDetailViewModel> { viewModelFactory }

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var binding: FragmentFavoriteListBinding

    override val tabIconId: Int
        get() = R.drawable.ic_favorite_pink_24dp

    override val tabTitle: String
        get() = "Favorite"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.restaurantStreet.observe(viewLifecycleOwner) {
            favoriteAdapter.restaurantStreet = it
        }

        binding.progressBar.show()
        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding.progressBar.apply { if (it) show() else hide() }
        }

        binding.favoriteListRecyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = favoriteAdapter.apply {
                onClickListener = RestaurantCardView.OnClickListener { it, restaurantData ->
                    sharedViewModel.showDetail(restaurantData.id)

                    it.findNavController().navigate(R.id.restaurantDetail)
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()

        viewModel.walkFavoriteRestaurantStreet()
    }
}