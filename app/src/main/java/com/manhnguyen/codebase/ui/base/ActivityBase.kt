package com.manhnguyen.codebase.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.manhnguyen.codebase.BR

abstract class ActivityBase : AppCompatActivity() {

    abstract fun getActivityDataBinding(): ViewDataBinding
    protected fun showHideErrorContainer(state: Boolean) {
        getActivityDataBinding().setVariable(BR.showError, state)
    }
}