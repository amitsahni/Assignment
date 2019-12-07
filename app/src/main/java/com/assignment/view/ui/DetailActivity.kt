package com.assignment.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assignment.R
import com.assignment.data.bean.Row
import com.assignment.databinding.ActivityDetailBinding
import com.assignment.extension.extraSerializable


class DetailActivity : AppCompatActivity() {

    companion object {
        const val MODEL = "model"
        fun getBundle(model: Row) = Bundle().apply {
            putSerializable(MODEL, model)
        }
    }

    private val model by extraSerializable(MODEL, Row())
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = model
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}