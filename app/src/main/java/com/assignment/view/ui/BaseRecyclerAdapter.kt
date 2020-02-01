package com.assignment.view.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerAdapter<VH : RecyclerView.ViewHolder, T> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal var count = 0
    internal var clickListener: ((Item<T>) -> Unit?)? = null

    var list = emptyList<T>()
        set(items) {
            val diffCallback = DiffCallback(items, field)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = items
            count = list.size
            // this.notifyDataSetChanged()
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateItemViewHolder(viewGroup, viewType)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindItemViewHolder(holder as VH, position)
    }

    fun getItem(position: Int): T {
        return list[position]
    }

    fun click(f: (Item<T>) -> Unit) {
        clickListener = f
    }

    abstract fun onCreateItemViewHolder(viewGroup: ViewGroup, viewType: Int): VH

    abstract fun onBindItemViewHolder(holder: VH, position: Int)
}

data class Item<T>(
    val position: Int,
    val model: T
)

class Holder(view: View) : RecyclerView.ViewHolder(view)

private class DiffCallback<T>(private val newList: List<T>, private val oldList: List<T>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}

