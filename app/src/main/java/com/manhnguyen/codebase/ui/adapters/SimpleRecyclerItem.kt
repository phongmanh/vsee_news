package com.manhnguyen.codebase.ui.adapters

import androidx.databinding.ViewDataBinding

abstract class SimpleRecyclerItem {

    abstract var adapter: BindableRecycleViewAdapter
    abstract fun getLayout(): Int
    fun getViewType() = getLayout()
    abstract fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder

}