package com.revoluttest.domain.repo

import com.revoluttest.domain.model.Currency
import io.reactivex.Observable
import io.reactivex.Single

interface CurrenciesRepository {
    fun getRatesMap(baseCurrency: Currency) : Observable<Map<String, Double>>
}