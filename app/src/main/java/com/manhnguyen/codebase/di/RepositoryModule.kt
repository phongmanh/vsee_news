package com.manhnguyen.codebase.di

import com.manhnguyen.codebase.data.repository.ConfigRepository
import com.manhnguyen.codebase.data.repository.MovieDataSource
import com.manhnguyen.codebase.data.repository.MovieRepository
import org.koin.dsl.module

class RepositoryModule {

    companion object {
        val modules = module {
            single { MovieRepository(get()) }
            single { ConfigRepository(get(), get()) }
        }
    }

}