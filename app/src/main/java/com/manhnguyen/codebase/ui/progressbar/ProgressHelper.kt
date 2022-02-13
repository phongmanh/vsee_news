package com.manhnguyen.codebase.ui.progressbar

import android.view.View
import android.view.Window
import android.view.WindowManager

interface ProgressHelper {

    fun getProgressDialog(): ProgressDialog

    fun getActivityWindow(): Window

    fun setProgressMessage(message: String) {
        getProgressDialog().message = message
    }

    fun showProgressBar(message: String = "Loading") {
        setProgressMessage(message)
        getProgressDialog().visibility = View.VISIBLE
        getActivityWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideProgressBar() {
        getProgressDialog().visibility = View.GONE
        getActivityWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}