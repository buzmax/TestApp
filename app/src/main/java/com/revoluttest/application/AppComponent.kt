package com.revoluttest.application

import android.app.Application
import com.revoluttest.view.di.CurrenciesListModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, CurrenciesListModule::class])
interface AppComponent  {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: RevolutTestApplication)
}