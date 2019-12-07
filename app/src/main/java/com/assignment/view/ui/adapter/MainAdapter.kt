package com.assignment.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.data.bean.Row
import com.assignment.databinding.ItemInfoBinding
import com.assignment.view.ui.BaseRecyclerAdapter
import com.assignment.view.ui.Item

class MainAdapter : BaseRecyclerAdapter<MainAdapter.Holder, Row>() {

    class Holder(val view: ItemInfoBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateItemViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemInfoBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindItemViewHolder(holder: Holder, position: Int) {
        val model = getItem(position)
        holder.view.model = model
        holder.view.root.setOnClickListener {
            clickListener?.invoke(Item(position, model))
        }
    }
}