package com.manhnguyen.codebase.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.ui.movie.NewsActivity
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (!isTaskRoot) {
            finish()
            return
        }

        lifecycleScope.launchWhenResumed {
            delay(500)
            startMapsActivity()
            finish()
        }
    }

    private fun startMapsActivity() = startActivity(NewsActivity.newIntent(this))
}