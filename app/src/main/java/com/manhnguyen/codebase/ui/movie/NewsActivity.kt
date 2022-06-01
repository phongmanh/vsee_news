package com.manhnguyen.codebase.ui.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.News
import com.manhnguyen.codebase.databinding.ActivityNewsBinding
import com.manhnguyen.codebase.system.networking.NetworkViewModel
import com.manhnguyen.codebase.ui.ToolbarHelper
import com.manhnguyen.codebase.ui.adapters.SimpleRecycleViewPagingAdapter
import com.manhnguyen.codebase.ui.adapters.SimpleRecyclerAdapter
import com.manhnguyen.codebase.ui.adapters.news.NewsItem
import com.manhnguyen.codebase.ui.base.ActivityBase
import com.manhnguyen.codebase.ui.progressbar.ProgressDialog
import com.manhnguyen.codebase.ui.progressbar.ProgressHelper
import com.manhnguyen.codebase.ui.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class NewsActivity : ActivityBase(), ProgressHelper, ToolbarHelper {


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

    private lateinit var binding: ActivityNewsBinding
    private val newsViewModel: NewsViewModel by inject()
    private val networkViewModel: NetworkViewModel by inject()

    private var navItemSelected: Int = -1
    private var waitingForNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ButterKnife.bind(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(
            ActivityLifecycle(this)
        )

        initializeToolbar(resources.getString(R.string.new_toolbar_title), false)
        navigationAction()
        initializePullDownToRefresh()

        networkViewModel.getInternetState().observe(this) { networkState ->
            //handleNetwork(networkState)
        }
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

    private fun makeMovieAdapter(news: List<News>) {
        rv_movie?.apply {
            adapter = SimpleRecyclerAdapter().apply {
                var index = 0
                news.forEach { data ->
                    setItem(NewsItem(data, this), index)
                    index++
                }
            }
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            layoutManager = LinearLayoutManager(this@NewsActivity)
        }
    }


    private val pagingAdapter: SimpleRecycleViewPagingAdapter = SimpleRecycleViewPagingAdapter()
    private suspend fun loadingDataPaging() {
        rv_movie.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            layoutManager = LinearLayoutManager(this@NewsActivity)
            adapter = pagingAdapter
        }
        pagingAdapter.addLoadStateListener { loadStates ->
            when (loadStates.refresh) {
                is LoadState.Loading -> {
                    showProgressBar()
                    showHideErrorContainer(false)
                }
                is LoadState.Error -> {
                    if (pagingAdapter.itemCount <= 0)
                        showHideErrorContainer(true)
                    hideProgressBar()
                }
                !is LoadState.Loading -> {
                    showHideErrorContainer(false)
                    hideProgressBar()
                }
            }
        }
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            newsViewModel.loadNews(pagingAdapter)
                .collect {
                    pagingAdapter.submitData(it)
                }
        }
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
                        R.id.home_item -> lifecycleScope.launch { loadingDataPaging() }
                    }
                    navItemSelected = item.itemId
                    return@OnNavigationItemSelectedListener true
                }

            binding.movieNavigation.setOnNavigationItemSelectedListener(listener)
            binding.movieNavigation.selectedItemId = R.id.home_item

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, NewsActivity::class.java)
    }


}




