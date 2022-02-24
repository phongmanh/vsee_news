package com.manhnguyen.codebase.ui.song

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.data.model.Result
import com.manhnguyen.codebase.ui.base.ActivityBase
import com.manhnguyen.codebase.ui.progressbar.ProgressDialog
import com.manhnguyen.codebase.ui.progressbar.ProgressHelper
import com.manhnguyen.codebase.ui.viewmodels.SongsViewModel
import org.koin.android.ext.android.inject

class SongActivity : ActivityBase(), ProgressHelper {

    val songsViewModel: SongsViewModel  by inject()
    override fun getActivityDataBinding(): ViewDataBinding {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
    }

    private fun loadingData(){
        songsViewModel.getSongs().observe(this,{ result ->
            when(result){
                is Result.Loading -> showProgressBar()
                is Result.Success -> {
                    // TODO

                    hideProgressBar()
                }
                is Result.Error -> {
                    hideProgressBar()
                }
            }
        })
    }

    override fun getProgressDialog(): ProgressDialog {
        TODO("Not yet implemented")
    }

    override fun getActivityWindow(): Window {
        TODO("Not yet implemented")
    }
}