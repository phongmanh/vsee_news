package com.manhnguyen.codebase.di

import com.manhnguyen.codebase.ui.viewmodels.MapsViewModel
import com.manhnguyen.codebase.system.locations.LocationViewModel
import com.manhnguyen.codebase.system.networking.NetworkViewModel
import com.manhnguyen.codebase.ui.viewmodels.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {

    companion object {
        val modules = module {
            viewModel { LocationViewModel() }
            viewModel { MapsViewModel(get(), get(), get()) }
            viewModel { MovieViewModel(get(), get()) }
            viewModel { NetworkViewModel(get()) }
        }
    }

}