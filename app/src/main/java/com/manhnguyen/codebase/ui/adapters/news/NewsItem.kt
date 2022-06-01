package com.manhnguyen.codebase.ui.adapters.news

import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.BR
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.News
import com.manhnguyen.codebase.ui.adapters.BindableRecycleViewAdapter
import com.manhnguyen.codebase.ui.adapters.BindableViewHolder
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerItem
import com.manhnguyen.codebase.ui.movie.NewsDetailsActivity

class NewsItem(val news: News, override var adapter: BindableRecycleViewAdapter) :
    SimpleRecyclerItem() {

    override fun getLayout(): Int {
        return R.layout.news_item_layout
    }

    override fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder = {
        MovieHolder(it)
    }

    inner class MovieHolder(private val binding: ViewDataBinding) : BindableViewHolder(binding) {
        override fun bind(item: Any) {
            when (item) {
                is NewsItem -> {
                    binding.setVariable(BR.newsDetail, item.news)
                    itemView.setOnClickListener {
                        /*it.context.startActivity(NewsDetailsActivity.newIntent(it.context, item.news.newsId))*/
                    }
                }
            }
        }
    }
}