package com.assignment.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment.R
import com.assignment.data.EventObserver
import com.assignment.data.Result
import com.assignment.databinding.ActivityMainBinding
import com.assignment.extension.startActivity
import com.assignment.view.ui.adapter.MainAdapter
import com.assignment.view.vm.MainAction
import com.assignment.view.vm.MainVM
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainVM: MainVM by viewModel()
    private val adapter by lazy {
        MainAdapter()
    }
    private val binding by lazy {
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        observe()
        binding.recyclerView.adapter = adapter

        mainVM.onAction(MainAction.UserInfo)
        swipeRefresh.setOnRefreshListener {
            mainVM.onAction(MainAction.UserInfo)
        }

        adapter.click {
            startActivity<DetailActivity> {
                putExtras(DetailActivity.getBundle(it.model))
            }
        }
    }

    private fun observe() {
        mainVM.fetchInfoLiveData.observe(this, EventObserver {
            when (it) {
                is Result.Loading -> {
                    swipeRefresh.isRefreshing = true
                }
                is Result.Success -> {
                    swipeRefresh.isRefreshing = false
                    adapter.list = it.data.rows ?: emptyList()
                }
                is Result.Error -> {
                    swipeRefresh.isRefreshing = false
                    adapter.list = emptyList()
                }
            }
        })
    }
}