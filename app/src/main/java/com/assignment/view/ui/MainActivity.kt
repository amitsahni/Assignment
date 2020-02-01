package com.assignment.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.assignment.R
import com.assignment.databinding.ActivityMainBinding
import com.assignment.extension.startActivity
import com.assignment.view.ui.adapter.MainAdapter
import com.assignment.view.vm.MainEvent
import com.assignment.view.vm.MainState
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

        mainVM.onAction(MainEvent.FetchEvent)
        swipeRefresh.setOnRefreshListener {
            mainVM.onAction(MainEvent.FetchEvent)
        }
    }

    private fun observe() {
        mainVM.fetchInfoLiveData.observe(this, Observer {
            when (it) {
                is MainState.Loading -> {
                    swipeRefresh.isRefreshing = true
                }
                is MainState.Success -> {
                    swipeRefresh.isRefreshing = false
                    adapter.list = it.data ?: emptyList()
                }
                is MainState.Error -> {
                    swipeRefresh.isRefreshing = false
                    adapter.list = emptyList()
                }
                is MainState.Click -> {
                    startActivity<DetailActivity> {
                        putExtras(DetailActivity.getBundle(it.row))
                    }
                }
            }
        })
    }
}