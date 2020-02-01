package com.assignment.view.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.databinding.ItemInfoBinding
import com.assignment.view.ui.BaseRecyclerAdapter
import com.assignment.view.vm.RowItem

class MainAdapter : BaseRecyclerAdapter<MainAdapter.Holder, RowItem>() {

    class Holder(val view: ItemInfoBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateItemViewHolder(viewGroup: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemInfoBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindItemViewHolder(holder: Holder, position: Int) {
        val model = getItem(position)
        holder.view.model = model
    }
}