package com.manhnguyen.codebase.ui.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.MovieInfo
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.databinding.ActivityMovieBinding
import com.manhnguyen.codebase.system.networking.NetworkViewModel
import com.manhnguyen.codebase.ui.ToolbarHelper
import com.manhnguyen.codebase.ui.adapters.SimpleRecycleViewPagingAdapter
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerAdapter
import com.manhnguyen.codebase.ui.adapters.movies.AdvertisementItem
import com.manhnguyen.codebase.ui.adapters.movies.MovieItem
import com.manhnguyen.codebase.ui.base.ActivityBase
import com.manhnguyen.codebase.ui.progressbar.ProgressDialog
import com.manhnguyen.codebase.ui.progressbar.ProgressHelper
import com.manhnguyen.codebase.ui.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class MovieActivity : ActivityBase(), ProgressHelper, ToolbarHelper {


    override fun getAppCompatActivity(): AppCompatActivity {
        return this
    }

    override fun getToolBar(): Toolbar? {
        return mainToolbar.toolbar
    }

    override fun getBinding(): ViewDataBinding {
        return binding
    }

    override fun getProgressDialog(): ProgressDialog {
        return progress_bar
    }

    override fun getActivityWindow(): Window {
        return window
    }

    override fun getActivityDataBinding(): ViewDataBinding {
        return binding
    }

    private lateinit var binding: ActivityMovieBinding
    private val movieViewModel: MovieViewModel by inject()
    private val networkViewModel: NetworkViewModel by inject()

    private var navItemSelected: Int = -1
    private var waitingForNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ButterKnife.bind(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(
            ActivityLifecycle(this)
        )

        initializeToolbar(resources.getString(R.string.movie_toolbar_title), false)
        navigationAction()
        initializePullDownToRefresh()

        networkViewModel.getInternetState().observe(this, { networkState ->
            handleNetwork(networkState)
        })
    }

    /**
     *  Handling UI with network state
     * @param networkState
     */
    private fun handleNetwork(networkState: Boolean) {
        if (networkState && waitingForNetwork) {
            waitingForNetwork = false
            showHideErrorContainer(false)
        } else if (!networkState) {
            showHideErrorContainer(true)
            waitingForNetwork = true

        }
    }

    /**
     * Pull down screen to refresh data
     */
    private fun initializePullDownToRefresh() {
        swipe_refresh_layout.apply {
            setOnRefreshListener {
                isRefreshing = false
                val currentItemSelected = navItemSelected
                navItemSelected = -1
                binding.movieNavigation.selectedItemId = currentItemSelected
            }
        }
    }

    /**
     * get Now Playing movies data
     * @param moviesInfo
     */

    private fun makeMovieAdapter(moviesInfo: List<MovieInfo>) {
        rv_movie?.apply {
            adapter = SimpleRecyclerAdapter().apply {
                var index = 0
                moviesInfo.forEach { movieInfo ->
                    movieInfo.imagePosterUrl =
                        movieViewModel.api.imagePosterBaseUrl + "/${movieInfo.poster_path}"
                    if ((index + 1) % 4 == 0) {
                        setItem(AdvertisementItem(this), index)
                    } else {
                        setItem(MovieItem(movieInfo, this), index)
                    }
                    index++
                }
            }
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            layoutManager = LinearLayoutManager(this@MovieActivity)
        }
    }

    /**
     * get Now Playing data from server
     */
    private fun loadingNowPlayingData() {
        movieViewModel.getNowPlaying(1).observe(this, {
            when (it) {
                is Result.Loading -> showProgressBar()
                is Result.Success -> {
                    showHideErrorContainer(false)
                    makeMovieAdapter(it.data.results)
                    hideProgressBar()
                }
                is Result.Error -> {
                    showHideErrorContainer(true)
                    hideProgressBar()
                }
            }
        })
    }

    /**
     * get Top Rated movies data from server
     */
    private fun loadingTopRatedData() {
        movieViewModel.getTopRated(1).observe(this, {
            when (it) {
                is Result.Loading -> showProgressBar()
                is Result.Success -> {
                    showHideErrorContainer(false)
                    //makeMovieAdapter(it.data.results)
                    hideProgressBar()
                }
                is Result.Error -> {
                    showHideErrorContainer(true)
                    hideProgressBar()
                }
            }
        })
    }

    /**
     * Navigation item actions
     */
    private fun navigationAction() {
        try {
            val listener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    if (navItemSelected == item.itemId) return@OnNavigationItemSelectedListener true
                    when (item.itemId) {
                        R.id.now_playing_item -> loadingNowPlayingData()
                        R.id.top_rate_item -> loadingTopRatedData()
                    }
                    navItemSelected = item.itemId
                    return@OnNavigationItemSelectedListener true
                }

            binding.movieNavigation.setOnNavigationItemSelectedListener(listener)
            binding.movieNavigation.selectedItemId = R.id.now_playing_item

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MovieActivity::class.java)
    }


}




