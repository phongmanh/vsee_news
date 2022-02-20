package com.manhnguyen.codebase.di

import com.manhnguyen.codebase.system.locations.FusedLocationService
import com.manhnguyen.codebase.system.locations.LocationService
import org.koin.dsl.module

abstract class ServiceModule {
    companion object {
        val serviceModule = module {
            single { LocationService() }
            single { FusedLocationService() }
        }
    }
}