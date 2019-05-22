package com.revoluttest.domain.usecase

import com.revoluttest.domain.model.Currency
import com.revoluttest.domain.repo.CurrenciesRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetCurrenciesRatesStreamUseCase @Inject constructor(
    private val currenciesRepository: CurrenciesRepository
) {
    fun execute(baseCurrency: Currency): Observable<Map<String, Double>> {
        return Observable.interval(1, TimeUnit.SECONDS)
            .flatMap { currenciesRepository.getRatesMap(baseCurrency) }
            .subscribeOn(Schedulers.io())
    }
}