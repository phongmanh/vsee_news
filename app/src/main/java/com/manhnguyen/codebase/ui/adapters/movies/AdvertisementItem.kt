package com.manhnguyen.codebase.ui.adapters.movies

import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.BR
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.MovieInfo
import com.manhnguyen.codebase.ui.adapters.BindableRecycleViewAdapter
import com.manhnguyen.codebase.ui.adapters.BindableViewHolder
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerAdapter
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerItem

class AdvertisementItem(override var adapter: BindableRecycleViewAdapter) : SimpleRecyclerItem() {

    override fun getLayout(): Int {
        return R.layout.advertisement_item_layout
    }

    override fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder = {
        AdvertHolder(it)
    }

    inner class AdvertHolder(private val binding: ViewDataBinding) : BindableViewHolder(binding) {
        override fun bind(item: Any) {
            when (item) {
                is AdvertisementItem -> {

                }
            }
        }
    }
}