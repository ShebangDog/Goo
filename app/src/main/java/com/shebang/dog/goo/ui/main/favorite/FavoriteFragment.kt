package com.shebang.dog.goo.ui.main.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentFavoriteListBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ui.base.TabbedFragment
import com.shebang.dog.goo.ui.common.widget.RestaurantCardView
import com.shebang.dog.goo.ui.main.detail.RestaurantDetailViewModel
import com.shebang.dog.goo.ui.main.street.RestaurantStreetViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class FavoriteFragment : TabbedFragment(R.layout.fragment_favorite_list) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by activityViewModels<RestaurantStreetViewModel> { viewModelFactory }
    private val sharedViewModel by activityViewModels<RestaurantDetailViewModel> { viewModelFactory }

    private val favoriteAdapter = FavoriteAdapter()

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
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteListRecyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = favoriteAdapter.apply {
                onClickListener = RestaurantCardView.OnClickListener { it, restaurantData ->
                    sharedViewModel.showDetail(restaurantData.id)

                    it.findNavController().navigate(R.id.restaurantDetail)
                }
            }
        }

        binding.viewModel = viewModel
        viewModel.favoriteList.observe(viewLifecycleOwner) {
            favoriteAdapter.restaurantDataList = it
        }

        binding.progressBar.show()
        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding.progressBar.apply { if (it) show() else hide() }
        }
    }
}