package com.manhnguyen.codebase.di

import com.manhnguyen.codebase.system.locations.LocationViewModel
import com.manhnguyen.codebase.system.networking.NetworkViewModel
import com.manhnguyen.codebase.ui.viewmodels.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {

    companion object {
        val modules = module {
            viewModel { LocationViewModel() }
            viewModel { NewsViewModel(get(), get()) }
            viewModel { NetworkViewModel(get()) }
        }
    }

}