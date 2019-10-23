package com.rodrixan.projects.technicaltests.shieldherolist.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

data class ViewWrapper<V : View>(val view: V) : RecyclerView.ViewHolder(view)

interface RecyclerViewBaseBinder<T, V : View> {
    fun bind(data: T, itemView: V, position: Int)
}

abstract class RecyclerViewBaseAdapter<T, V : View>(diffCallback: BaseAdapterDiffCallback<T> = DefaultAdapterDiffCallback()) : ListAdapter<T, ViewWrapper<V>>(
        diffCallback), RecyclerViewBaseBinder<T, V> {

    var onClickListener: ((data: T, view: View) -> Unit)? = null

    protected abstract fun onCreateItemView(parent: ViewGroup, viewType: Int): V

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewWrapper<V> {
        return ViewWrapper(onCreateItemView(parent, viewType))
    }

    override fun onBindViewHolder(holder: ViewWrapper<V>, position: Int) {
        bind(getItem(position), holder.view, position)
    }

    fun updateItems(newItems: List<T>) {
        submitList(newItems)
    }
}

abstract class BaseAdapterDiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem,
            newItem)
}

private class DefaultAdapterDiffCallback<T> : BaseAdapterDiffCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem?.equals(newItem) == true
}
