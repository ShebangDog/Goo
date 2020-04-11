package com.shebang.dog.goo.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.ActivityMainBinding
import com.shebang.dog.goo.util.PermissionGranter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navigationController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolBar)
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
        navigationController.navigate(R.id.aboutActivity)
    }
}