package com.revoluttest.viewmodel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revoluttest.viewmodel.CurrenciesListViewModel
import com.revoluttest.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface CurrenciesListViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CurrenciesListViewModel::class)
    fun currenciesListViewModel(viewModel: CurrenciesListViewModel): ViewModel
}