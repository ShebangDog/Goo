package com.shebang.dog.goo.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shebang.dog.goo.databinding.ActivityRestaurantListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityRestaurantListBinding.inflate(this.layoutInflater)
        setContentView(this.binding.root)

        this.binding.apply {
            this.restaurantListRecyclerView.layoutManager = LinearLayoutManager(this@ListActivity)
        }



    }
}
