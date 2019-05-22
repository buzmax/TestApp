package com.revoluttest.view.di

import com.revoluttest.data.di.BinderModule
import com.revoluttest.data.di.DataModule
import com.revoluttest.view.activity.CurrenciesListActivity
import com.revoluttest.viewmodel.di.CurrenciesListViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface CurrenciesListModule {

    @ContributesAndroidInjector(
        modules = [DataModule::class, BinderModule::class, CurrenciesListViewModelModule::class]
    )
    fun currenciesListActivityContributor(): CurrenciesListActivity
}