package com.revoluttest.data.di

import com.revoluttest.data.repo.CurrenciesRepositoryImpl
import com.revoluttest.domain.repo.CurrenciesRepository
import dagger.Binds
import dagger.Module

@Module
internal interface BinderModule {
    @Binds
    fun bindsCurrenciesRepository(bagRepositoryImpl: CurrenciesRepositoryImpl): CurrenciesRepository
}