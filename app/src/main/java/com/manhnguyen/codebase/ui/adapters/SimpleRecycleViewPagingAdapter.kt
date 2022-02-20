package com.manhnguyen.codebase.ui.adapters

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

class SimpleRecycleViewPagingAdapter: BindableRecycleViewPagingAdapter(ITEM_COMPARATOR) {

    private var items: MutableList<SimpleRecyclerPagingItem> = mutableListOf()
    val viewTypeAndHolder: MutableMap<Int, (binding: ViewDataBinding) -> BindableViewHolder> =
        mutableMapOf()

    override fun getViewItem(position: Int): Any {
        return getItem(position)!!
    }

    override fun getLayout(position: Int): Int {
        return getItem(position)!!.getLayout()
    }

    override fun getViewHolder(binding: ViewDataBinding, viewType: Int): BindableViewHolder? {
        return viewTypeAndHolder[viewType]?.invoke(binding)
    }

    open fun setItems(items: List<SimpleRecyclerPagingItem>) {
        this.items = items.toMutableList()
        items.forEach {
            viewTypeAndHolder[it.getViewType()] = it.getViewHolderProvider()
        }
        notifyDataSetChanged()
    }

    open fun setItem(item: SimpleRecyclerPagingItem, index: Int) {
        this.items.add(index, item)
        viewTypeAndHolder[item.getViewType()] = item.getViewHolderProvider()
        notifyDataSetChanged()
    }


    companion object {
        val ITEM_COMPARATOR = object :
            DiffUtil.ItemCallback<SimpleRecyclerPagingItem>() {
            override fun areItemsTheSame( oldItem: SimpleRecyclerPagingItem, newItem: SimpleRecyclerPagingItem ): Boolean {
               return oldItem.getItemDataId() == newItem.getItemDataId()
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: SimpleRecyclerPagingItem, newItem: SimpleRecyclerPagingItem ): Boolean {
                return oldItem.getItemData().equals(newItem.getItemData())
            }

        }
    }


}