package com.manhnguyen.codebase.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.BR
import com.manhnguyen.codebase.R
import kotlinx.android.synthetic.main.toolbar_layout.view.*

interface ToolbarHelper {

    fun getAppCompatActivity(): AppCompatActivity
    fun getToolBar(): Toolbar? = null
    fun getBinding(): ViewDataBinding? = null


    fun initializeToolbar(title: String, navigation: Boolean) {
        getToolBar()?.let { toolbar ->
            getBinding()?.let {
                it.setVariable(BR.title, title)
                toolbar.setting_button.setOnClickListener {

                }
            }
            with(getAppCompatActivity()) {
                setSupportActionBar(toolbar)
                supportActionBar?.let {
                    it.setDisplayShowTitleEnabled(false)
                    if (navigation) {
                        it.setDisplayHomeAsUpEnabled(true)
                        it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24)
                    }
                }
                toolbar.setNavigationOnClickListener {
                    getAppCompatActivity().onBackPressed()
                }
            }
        }
    }


}