package com.revoluttest.viewmodel

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CurrencyListUserInteractions @Inject constructor() {
    private val publishSubject = PublishSubject.create<CurrenciesListViewModel.Event>()

    fun events(): Observable<CurrenciesListViewModel.Event> = publishSubject.hide()

    fun changeBaseCurrency(currency: CurrencyItemViewModel) {
        publishSubject.onNext(CurrenciesListViewModel.Event.OnBaseCurrencyChanged(currency))
    }

    fun changeExchangeAmount(amount: Double) {
        publishSubject.onNext(CurrenciesListViewModel.Event.OnExchangeAmountChanged(amount))
    }
}