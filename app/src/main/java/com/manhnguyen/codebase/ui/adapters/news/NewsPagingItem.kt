package com.manhnguyen.codebase.ui.adapters.news

import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.BR
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.News
import com.manhnguyen.codebase.ui.adapters.*
import com.manhnguyen.codebase.ui.movie.NewsDetailsActivity
import java.util.*

class NewsPagingItem(
    private val news: News,
    override var adapter: BindableRecycleViewPagingAdapter
) : SimpleRecyclerPagingItem() {

    override fun getItemData(): News {
        return news
    }

    override fun getItemDataId(): UUID {
        return news.newsId
    }

    override fun getLayout(): Int {
        return R.layout.news_item_layout
    }

    override fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder = {
        NewsPagingHolder(it)
    }

    inner class NewsPagingHolder(private val binding: ViewDataBinding) :
        BindableViewHolder(binding) {
        override fun bind(item: Any) {
            when (item) {
                is NewsPagingItem -> {
                    binding.setVariable(BR.news, item.news)
                    itemView.setOnClickListener {
                         it.context.startActivity(NewsDetailsActivity.newIntent(it.context, item.news.newsId))
                    }
                }
            }
        }
    }
}