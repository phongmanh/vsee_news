package com.manhnguyen.codebase.ui.adapters

import androidx.databinding.ViewDataBinding

abstract class SimpleRecyclerPagingItem {

    abstract var adapter: BindableRecycleViewPagingAdapter
    abstract fun getItemData(): Any
    abstract fun getItemDataId(): Any
    abstract fun getLayout(): Int
    fun getViewType() = getLayout()
    abstract fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder

}