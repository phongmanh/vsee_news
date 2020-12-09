package com.manhnguyen.codebase.di.component

import android.content.Context
import com.manhnguyen.codebase.ApplicationController
import com.manhnguyen.codebase.di.module.*
import com.manhnguyen.codebase.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [AppModule::class, APIServiceModule::class, SubComponentsModule::class
        , ServiceModule::class, AndroidInjectionModule::class, DatabaseModule::class],
    dependencies = []
)
interface AppComponent {

    fun loginComponent(): LoginComponent.Factory
    fun getContext(): Context

    @Component.Factory
    interface Factory {
        /* inject context to APIServiceModule */
        fun create(@BindsInstance context: Context): AppComponent
    }


    fun inject(mainActivity: MainActivity)
    fun inject(applicationController: ApplicationController)
}