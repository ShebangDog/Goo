package com.shebang.dog.goo.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.shebang.dog.goo.R
import com.shebang.dog.goo.data.model.about.AboutItem
import com.shebang.dog.goo.data.model.about.Summary
import com.shebang.dog.goo.data.model.about.Title
import com.shebang.dog.goo.databinding.AboutListItemBinding
import com.shebang.dog.goo.databinding.ActivityAboutBinding
import com.shebang.dog.goo.ui.base.MyDaggerAppCompatActivity
import javax.inject.Inject

class AboutActivity : MyDaggerAppCompatActivity(R.layout.activity_about) {
    @Inject
    lateinit var aboutAdapter: AboutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAboutBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_about)

        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.aboutListRecyclerview.apply {
            val linearLayoutManager = LinearLayoutManager(context)

            layoutManager = linearLayoutManager
            adapter = aboutAdapter

            addItemDecoration(
                DividerItemDecoration(
                    context,
                    linearLayoutManager.orientation
                )
            )
        }
    }

    class AboutAdapter @Inject constructor() :
        RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

        private val aboutList = listOf(
            AboutItem(
                title = Title("Open source licenses"),
                onClick = { showOssLicensesScreen(it.context) }),
            AboutItem(
                title = Title("Currently version"),
                summary = Summary("1.1")
            )
        )

        class AboutViewHolder(private val binding: AboutListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bindAbout(aboutItem: AboutItem) {
                binding.aboutItem = aboutItem
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AboutListItemBinding.inflate(inflater, parent, false)

            return AboutViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return aboutList.size
        }

        override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
            holder.bindAbout(aboutItem = aboutList[position])
        }

        private fun showOssLicensesScreen(context: Context) {
            val intent = Intent(context, OssLicensesMenuActivity::class.java)

            context.startActivity(intent)
        }

    }
}