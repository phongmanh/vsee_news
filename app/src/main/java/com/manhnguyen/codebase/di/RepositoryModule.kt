package com.manhnguyen.codebase.di

import com.manhnguyen.codebase.data.repository.NewsRepository
import org.koin.dsl.module

class RepositoryModule {

    companion object {
        val modules = module {
            single { NewsRepository(get()) }
        }
    }

}