package com.revoluttest.data.repo

import com.revoluttest.data.api.Api
import com.revoluttest.data.model.toCurrenciesMap
import com.revoluttest.domain.repo.CurrenciesRepository
import com.revoluttest.domain.model.Currency
import io.reactivex.Observable
import javax.inject.Inject

internal class CurrenciesRepositoryImpl @Inject constructor(
    private val api: Api
) : CurrenciesRepository {
    override fun getRatesMap(baseCurrency: Currency): Observable<Map<String, Double>> =
        api.getCurrencies(baseCurrency = baseCurrency.name)
            .map { it.toCurrenciesMap() }
            .onErrorReturn { emptyMap() }
}