package com.manhnguyen.codebase.ui.adapters.movies

import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.BR
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.MovieInfo
import com.manhnguyen.codebase.ui.adapters.BindableRecycleViewAdapter
import com.manhnguyen.codebase.ui.adapters.BindableViewHolder
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerAdapter
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerItem
import com.manhnguyen.codebase.ui.movie.MovieDetailsActivity

class MovieItem(val movieInfo: MovieInfo, override var adapter: BindableRecycleViewAdapter) :
    SimpleRecyclerItem() {

    override fun getLayout(): Int {
        return R.layout.movie_item_layout
    }

    override fun getViewHolderProvider(): (binding: ViewDataBinding) -> BindableViewHolder = {
        MovieHolder(it)
    }

    inner class MovieHolder(private val binding: ViewDataBinding) : BindableViewHolder(binding) {
        override fun bind(item: Any) {
            when (item) {
                is MovieItem -> {
                    binding.setVariable(BR.movieInfo, item.movieInfo)
                    itemView.setOnClickListener {
                        it.context.startActivity(MovieDetailsActivity.newIntent(it.context, item.movieInfo.movieId))
                    }
                }
            }
        }
    }
}