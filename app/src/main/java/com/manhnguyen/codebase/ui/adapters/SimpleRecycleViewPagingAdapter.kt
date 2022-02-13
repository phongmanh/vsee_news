package com.manhnguyen.codebase.ui.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

class SimpleRecycleViewPagingAdapter: BindableRecycleViewPagingAdapter(ITEM_COMPARATOR) {

    private var items: MutableList<SimpleRecyclerItem> = mutableListOf()
    private val viewTypeAndHolder: MutableMap<Int, (binding: ViewDataBinding) -> BindableViewHolder> =
        mutableMapOf()

    override fun getViewItem(position: Int): Any {
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


    companion object {
        val ITEM_COMPARATOR = object :
            DiffUtil.ItemCallback<SimpleRecyclerItem>() {
            override fun areItemsTheSame( oldItem: SimpleRecyclerItem, newItem: SimpleRecyclerItem ): Boolean {
               return oldItem.getViewType() == newItem.getViewType()
            }

            override fun areContentsTheSame( oldItem: SimpleRecyclerItem, newItem: SimpleRecyclerItem ): Boolean {
                return oldItem.equals(newItem)
            }

        }
    }


}