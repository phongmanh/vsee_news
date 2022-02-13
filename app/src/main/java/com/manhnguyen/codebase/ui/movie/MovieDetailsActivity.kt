package com.manhnguyen.codebase.ui.movie

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.databinding.ActivityMovieDetailsBinding
import com.manhnguyen.codebase.ui.ToolbarHelper
import com.manhnguyen.codebase.ui.base.ActivityBase
import com.manhnguyen.codebase.ui.progressbar.ProgressDialog
import com.manhnguyen.codebase.ui.progressbar.ProgressHelper
import com.manhnguyen.codebase.ui.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.koin.android.ext.android.inject

class MovieDetailsActivity : ActivityBase(), ToolbarHelper, ProgressHelper {

    override fun getAppCompatActivity(): AppCompatActivity {
        return this
    }

    override fun getToolBar(): Toolbar? {
        return mainToolbar.toolbar
    }

    override fun getBinding(): ViewDataBinding {
        return binding
    }

    override fun getActivityDataBinding(): ViewDataBinding {
        return binding
    }

    lateinit var binding: ActivityMovieDetailsBinding
    private val movieViewModel: MovieViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeToolbar(resources.getString(R.string.movie_toolbar_title), true)
        getMovieDetails()

    }

    override fun getProgressDialog(): ProgressDialog {
        return progress_bar
    }

    override fun getActivityWindow(): Window {
        return window
    }

    private fun getMovieDetails() {
        showProgressBar()
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        if (movieId != -1) {
            movieViewModel.getMovieDetails(movieId).observe(this, {
                when (it) {
                    is Result.Loading -> showProgressBar()
                    is Result.Success -> {
                        showHideErrorContainer(false)
                        it.data.backdrop_pathUrl =
                            "${movieViewModel.api.imageBackDropBaseUrl}/${it.data.backdrop_path}"
                        binding.setVariable(BR.movieDetail, it.data)
                        binding.tvSeeAllAction.setOnClickListener {
                            Toast.makeText(this, "Show all casts and crews", Toast.LENGTH_LONG).show()
                        }
                        hideProgressBar()
                    }
                    is Result.Error -> {
                        showHideErrorContainer(true)
                        hideProgressBar()
                    }
                }
            })
        }
    }


    companion object {

        const val EXTRA_MOVIE_ID = "extra_movie_id"

        fun newIntent(context: Context, movieId: Int) =
            Intent(context, MovieDetailsActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }
    }
}