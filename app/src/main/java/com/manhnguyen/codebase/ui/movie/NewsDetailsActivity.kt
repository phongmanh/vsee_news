package com.manhnguyen.codebase.ui.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.manhnguyen.codebase.BR
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.News
import com.manhnguyen.codebase.databinding.ActivityNewsBinding
import com.manhnguyen.codebase.databinding.ActivityNewsDetailsBinding
import com.manhnguyen.codebase.ui.ToolbarHelper
import com.manhnguyen.codebase.ui.base.ActivityBase
import com.manhnguyen.codebase.ui.progressbar.ProgressDialog
import com.manhnguyen.codebase.ui.progressbar.ProgressHelper
import com.manhnguyen.codebase.ui.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.activity_news_details.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class NewsDetailsActivity : ActivityBase(), ToolbarHelper, ProgressHelper {

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

    lateinit var binding: ActivityNewsDetailsBinding
    private val newsViewModel: NewsViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeToolbar(resources.getString(R.string.movie_toolbar_title), true)
        getNewsDetails()

    }

    override fun getProgressDialog(): ProgressDialog {
        return progress_bar
    }

    override fun getActivityWindow(): Window {
        return window
    }

    private fun getNewsDetails() {
       lifecycleScope.launch {
           showProgressBar()
           val newsId = UUID.fromString(intent.getStringExtra(EXTRA_NEWS_ID))
            repeatOnLifecycle(Lifecycle.State.STARTED){
                newsViewModel.getNewsDetails(newsId)
                    .catch {

                    }
                    .collectLatest {
                    binding.setVariable(BR.newsDetail, it)
                    binding.showError = false
                    hideProgressBar()
                }
            }
       }
    }


    companion object {

        const val EXTRA_NEWS_ID = "extra_news_id"

        fun newIntent(context: Context, newsId: UUID) =
            Intent(context, NewsDetailsActivity::class.java).apply {
                putExtra(EXTRA_NEWS_ID, newsId.toString())
            }
    }
}