package com.shebang.dog.goo.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentRestaurantDetailBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ext.assistedViewModels
import com.shebang.dog.goo.ui.tab.MyDaggerFragment
import javax.inject.Inject

class RestaurantDetailFragment : MyDaggerFragment(R.layout.fragment_restaurant_detail) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by assistedViewModels {
        viewModelFactory.create(RestaurantDetailViewModel::class.java)
    }

    private lateinit var binding: FragmentRestaurantDetailBinding
    private val restaurantThumbnailAdapter = RestaurantThumbnailAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantDetailBinding.bind(view)

        val horizontalLinearLayoutManager = LinearLayoutManager(context).apply {
            orientation = RecyclerView.HORIZONTAL
        }

        binding.restaurantThumbnailRecyclerView.apply {
            adapter = restaurantThumbnailAdapter
            layoutManager = horizontalLinearLayoutManager
        }

        viewModel.restaurantData.observe(viewLifecycleOwner) { restaurantData ->
            restaurantData?.also { binding.restaurantNameTextView.text = it.name.value }
        }

        viewModel.restaurantImageUrlList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) binding.restaurantThumbnailRecyclerView.isVisible = false
            restaurantThumbnailAdapter.restaurantThumbnailList = it
        }

        val args: RestaurantDetailFragmentArgs by navArgs()
        val id = args.id ?: return

        viewModel.showDetail(id)
    }
}