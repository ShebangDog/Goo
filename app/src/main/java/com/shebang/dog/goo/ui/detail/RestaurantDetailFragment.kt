package com.shebang.dog.goo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentRestaurantDetailBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ui.tab.MyDaggerFragment
import javax.inject.Inject

class RestaurantDetailFragment : MyDaggerFragment(R.layout.fragment_restaurant_detail) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by activityViewModels<RestaurantDetailViewModel> { viewModelFactory }

    private lateinit var binding: FragmentRestaurantDetailBinding
    private val restaurantThumbnailAdapter = RestaurantThumbnailAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}