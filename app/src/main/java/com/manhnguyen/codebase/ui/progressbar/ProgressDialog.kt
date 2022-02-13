package com.manhnguyen.codebase.ui.progressbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.databinding.ProgressBarLayoutBinding

class ProgressDialog @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var message: String? = null
        set(value) {
            field = value
            binding.message = value
        }

    private var binding: ProgressBarLayoutBinding =
        DataBindingUtil.inflate<ProgressBarLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.progress_bar_layout, this, true
        ).apply {
            lifecycleOwner = context as LifecycleOwner
        }

    override fun getRootView(): View {
       return binding.root
    }

}
