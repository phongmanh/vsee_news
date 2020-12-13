package com.manhnguyen.codebase.di.module

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.manhnguyen.codebase.common.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module

class AppModule constructor(private val context: Context) {

    fun provideSchedulerProvider(): SchedulerProvider {
        return SchedulerProvider(
            Schedulers.io(),
            AndroidSchedulers.mainThread(),
            Schedulers.computation(),
            Schedulers.single(),
            Schedulers.trampoline()
        )
    }

    fun providerLocationManager(): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    fun fusedLocationProviderClient(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    companion object {
        val appModule = module {
            single { AppModule(get()) }
        }
    }

}

