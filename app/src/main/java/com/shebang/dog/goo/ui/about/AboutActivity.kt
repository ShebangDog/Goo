package com.shebang.dog.goo.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.shebang.dog.goo.R
import com.shebang.dog.goo.model.AboutItem
import com.shebang.dog.goo.model.Summary
import com.shebang.dog.goo.model.Title
import com.shebang.dog.goo.databinding.AboutListItemBinding
import com.shebang.dog.goo.databinding.ActivityAboutBinding
import com.shebang.dog.goo.ui.tab.MyDaggerAppCompatActivity
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
            AboutItem(title = Title("Currently version"), summary = Summary("1.1"))
        )

        class AboutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val binding = AboutListItemBinding.bind(view)
            private val context = view.context

            fun setAbout(about: AboutItem) {
                setTitle(about.title)

                about.summary.also {
                    if (it == null) binding.summaryTextView.isVisible = false
                    else setSummary(it)
                }

                about.onClick?.also { setOnClick(it) }
            }

            private fun setTitle(title: Title) {
                binding.titleTextView.text = title.value
            }

            private fun setSummary(summary: Summary) {
                binding.summaryTextView.text = summary.value
            }

            private fun setOnClick(onClick: (View) -> Unit) {
                binding.item.setOnClickListener(onClick)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
            val inflate = LayoutInflater.from(parent.context)
                .inflate(R.layout.about_list_item, parent, false)

            return AboutViewHolder(inflate)
        }

        override fun getItemCount(): Int {
            return aboutList.size
        }

        override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
            holder.setAbout(aboutList[position])
        }

        private fun showOssLicensesScreen(context: Context) {
            val intent = Intent(context, OssLicensesMenuActivity::class.java)

            context.startActivity(intent)
        }

    }
}