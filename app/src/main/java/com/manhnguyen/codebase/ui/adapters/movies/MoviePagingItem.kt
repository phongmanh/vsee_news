package com.manhnguyen.codebase.ui.adapters.movies

import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.BR
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.MovieInfo
import com.manhnguyen.codebase.ui.adapters.*
import com.manhnguyen.codebase.ui.movie.MovieDetailsActivity

class MoviePagingItem(val movieInfo: MovieInfo ,override var adapter: BindableRecycleViewPagingAdapter) : SimpleRecyclerPagingItem() {

    override fun getItemData(): MovieInfo {
        return  movieInfo
    }

    override fun getItemDataId(): Int {
        return movieInfo.movieId
    }

    override fun getLayout(): Int {
       return R.layout.movie_item_layout
    }

    override fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder = {
        MoviePagingHolder(it)
    }

    inner class MoviePagingHolder(private val binding: ViewDataBinding) : BindableViewHolder(binding) {
        override fun bind(item: Any) {
            when (item) {
                is MoviePagingItem -> {
                    binding.setVariable(BR.movieInfo, item.movieInfo)
                    itemView.setOnClickListener {
                        it.context.startActivity(MovieDetailsActivity.newIntent(it.context, item.movieInfo.movieId))
                    }
                }
            }
        }
    }
}