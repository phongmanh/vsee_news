package com.manhnguyen.codebase.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindableRecycleViewAdapter : RecyclerView.Adapter<BindableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        try {
            return getViewHolder(binding, viewType)!!
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayout(position)
    }

    abstract fun getItem(position: Int): Any
    abstract fun getLayout(position: Int): Int
    abstract fun getViewHolder(binding: ViewDataBinding, viewType: Int): BindableViewHolder?
}