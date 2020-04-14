package com.shebang.dog.goo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.FragmentRestaurantDetailBinding
import com.shebang.dog.goo.di.ViewModelFactory
import com.shebang.dog.goo.ext.assistedViewModels
import com.shebang.dog.goo.model.restaurant.Id
import com.shebang.dog.goo.ui.tab.MyDaggerFragment
import javax.inject.Inject

class RestaurantDetailFragment : MyDaggerFragment(R.layout.fragment_restaurant_detail) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by assistedViewModels {
        viewModelFactory.create(RestaurantDetailViewModel::class.java)
    }

    private lateinit var binding: FragmentRestaurantDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantDetailBinding.bind(view)

        viewModel.restaurantData.observe(viewLifecycleOwner) { restaurantData ->
            restaurantData?.also {
                binding.restaurantNameTextView.text = it.name.value

                Glide.with(view)
                    .load(it.imageUrl)
                    .into(binding.thumbnailImageView)
            }
        }

        val args: RestaurantDetailFragmentArgs by navArgs()
        viewModel.showDetail(Id(args.id))
    }
}