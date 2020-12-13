package com.manhnguyen.codebase.di.module

import com.manhnguyen.codebase.data.repository.LoginRepository
import org.koin.dsl.module

class RepositoryModules {

    companion object {
        val modules = module {
            single { LoginRepository(get()) }
        }
    }

}