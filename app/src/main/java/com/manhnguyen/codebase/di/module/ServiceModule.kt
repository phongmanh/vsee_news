package com.manhnguyen.codebase.di.module

import com.manhnguyen.codebase.system.locations.FusedLocationService
import com.manhnguyen.codebase.system.locations.LocationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun providerLocationService(): LocationService

    @ContributesAndroidInjector
    abstract fun providerFusedLocationService(): FusedLocationService


}