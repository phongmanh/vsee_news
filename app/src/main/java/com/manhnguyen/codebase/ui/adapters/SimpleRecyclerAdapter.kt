package com.manhnguyen.codebase.ui.adapters

import androidx.databinding.ViewDataBinding

open class SimpleRecyclerAdapter : BindableRecycleViewAdapter() {

    private var items: MutableList<SimpleRecyclerItem> = mutableListOf()
    private val viewTypeAndHolder: MutableMap<Int, (binding: ViewDataBinding) -> BindableViewHolder> =
        mutableMapOf()

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getLayout(position: Int): Int {
        return items[position].getLayout()
    }

    override fun getViewHolder(binding: ViewDataBinding, viewType: Int): BindableViewHolder? {
        return viewTypeAndHolder[viewType]?.invoke(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open fun setItems(items: List<SimpleRecyclerItem>) {
        this.items = items.toMutableList()
        items.forEach {
            viewTypeAndHolder[it.getViewType()] = it.getViewHolderProvider()
        }
        notifyDataSetChanged()
    }

    open fun setItem(item: SimpleRecyclerItem, index: Int) {
        this.items.add(index, item)
        viewTypeAndHolder[item.getViewType()] = item.getViewHolderProvider()
        notifyDataSetChanged()
    }



}