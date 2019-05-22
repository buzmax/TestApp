package com.revoluttest.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.revoluttest.domain.model.Currency
import com.revoluttest.domain.usecase.GetCurrenciesRatesStreamUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.math.RoundingMode
import javax.inject.Inject

private const val DEFAULT_EXCHANGE_AMOUNT = 100.0

internal class CurrenciesListViewModel @Inject constructor(
    private val getCurrenciesRatesStreamUseCase: GetCurrenciesRatesStreamUseCase,
    private val userInteractions: CurrencyListUserInteractions
) : ViewModel(), LifecycleObserver {

    val items = ObservableField<MutableList<CurrencyItemViewModel>>(mutableListOf())
    val isEmpty = ObservableBoolean(false)
    private var baseCurrency = Currency.DEFAULT_CURRENCY
    private val compositeDisposable = CompositeDisposable()
    private var exchangeAmount = DEFAULT_EXCHANGE_AMOUNT

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startObserve() {
        compositeDisposable.add(getCurrenciesRatesStreamUseCase.execute(baseCurrency)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { handleUpdatedRates(it) }
        )
        compositeDisposable.add(userInteractions.events()
            .subscribe { handleEvent(it) }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopObserve() {
        compositeDisposable.clear()
    }

    // handling events from user
    private fun handleEvent(event: Event) {

        fun onExchangeAmountChanged(amount: Double) {
            exchangeAmount = amount
            items.get()?.onEach { it.updateExchangeAmount(exchangeAmount) }
        }

        fun changeBaseCurrency(newBaseCurrency: CurrencyItemViewModel) {

            fun recalculateExchangeAmount(newBaseCurrency: CurrencyItemViewModel) =
                (newBaseCurrency.exchangeAmount * newBaseCurrency.currency.exchangeRate)
                    .toBigDecimal()
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .toDouble()

            fun swapItems(
                list: MutableList<CurrencyItemViewModel>,
                newBaseCurrency: CurrencyItemViewModel
            ) {
                list[0].isBaseCurrency = false
                newBaseCurrency.isBaseCurrency = true
                list.remove(newBaseCurrency)
                list.add(0, newBaseCurrency)
            }

            items.get()?.let {
                stopObserve()
                baseCurrency = newBaseCurrency.currency
                exchangeAmount = recalculateExchangeAmount(newBaseCurrency)
                swapItems(it, newBaseCurrency)
                items.notifyChange()
                startObserve()
            }
        }

        return when (event) {
            is Event.OnExchangeAmountChanged -> onExchangeAmountChanged(event.amount)
            is Event.OnBaseCurrencyChanged -> changeBaseCurrency(event.baseCurrency)
        }
    }

    // handle response from the server
    private fun handleUpdatedRates(updatedRates: Map<String, Double>) {

        fun createList(map: Map<String, Double>) {

            fun Pair<String, Double>.toCurrency(): Currency = Currency(first, second)

            fun mapToViewModel(index: Int, pair: Pair<String, Double>): CurrencyItemViewModel =
                CurrencyItemViewModel(
                    pair.toCurrency(),
                    exchangeAmount,
                    index == 0,
                    userInteractions
                )

            if (map.isEmpty()) {
                isEmpty.set(true)
                return
            }

            items.set(
                map.toList()
                    .mapIndexed(::mapToViewModel)
                    .toMutableList()
            )
        }

        fun updateList(
            rates: Map<String, Double>,
            list: MutableList<CurrencyItemViewModel>
        ) {
            fun updateItem(
                viewModel: CurrencyItemViewModel,
                rates: Map<String, Double>
            ) {
                viewModel.updateItem(
                    rates.getOrDefault(
                        viewModel.currency.name.toString(),
                        -1.0
                    ), exchangeAmount
                )
            }

            if (rates.isEmpty()) {
                isEmpty.set(true)
                items.set(mutableListOf())
                return
            }

            list.onEach { updateItem(it, rates) }
            items.notifyChange()
        }

        items.get().takeIf { it.isNullOrEmpty().not() }
            ?.let { updateList(updatedRates, it) }
            ?: createList(updatedRates)
    }

    sealed class Event {
        class OnExchangeAmountChanged(val amount: Double) : Event()
        class OnBaseCurrencyChanged(val baseCurrency: CurrencyItemViewModel) : Event()
    }
}