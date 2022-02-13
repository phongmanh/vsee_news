package com.manhnguyen.codebase.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.ui.map.MapsActivity
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

    private fun startMapsActivity() = startActivity(MapsActivity.newIntent(this))
}