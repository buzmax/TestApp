package com.revoluttest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.databinding.ObservableField
import com.revoluttest.RxAndroidSchedulerRule
import com.revoluttest.domain.mockMap_1_BASE_AUD
import com.revoluttest.domain.mockMap_1_BASE_EUR
import com.revoluttest.domain.mockMap_2_BASE_EUR
import com.revoluttest.domain.model.Currency
import com.revoluttest.domain.usecase.GetCurrenciesRatesStreamUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.subjects.PublishSubject
import junit.framework.AssertionFailedError
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class CurrenciesListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val androidSchedulerRule = RxAndroidSchedulerRule()

    private val getCurrenciesRatesStreamUseCase = mockk<GetCurrenciesRatesStreamUseCase>(relaxed = true)
    private val userInteractions = mockk<CurrencyListUserInteractions>(relaxed = true)
    private lateinit var currenciesListViewModel: CurrenciesListViewModel
    private val currenciesUpdatedEvents = PublishSubject.create<Map<String, Double>>()
    private val userEvents = PublishSubject.create<CurrenciesListViewModel.Event>()

    @Before
    fun setUp() {
        every { getCurrenciesRatesStreamUseCase.execute(any()) } returns currenciesUpdatedEvents.hide()
        every { userInteractions.events() } returns userEvents.hide()
        currenciesListViewModel = CurrenciesListViewModel(getCurrenciesRatesStreamUseCase, userInteractions)
    }

    @Test
    fun `GIVEN a currencies map WHEN pass to view model THEN create list of view model items`() {

        //GIVEN
        val map = mockMap_1_BASE_EUR()

        //WHEN
        currenciesListViewModel.startObserve()
        currenciesUpdatedEvents.onNext(map)

        //THEN
        getItems(currenciesListViewModel.items)
            .onEach { assertEquals(map[it.currency.name], it.currency.exchangeRate) }
    }

    @Test
    fun `GIVEN an empty currencies map WHEN pass to view model THEN show error`() {

        //GIVEN
        val map = emptyMap<String, Double>()

        //WHEN
        currenciesListViewModel.startObserve()
        currenciesUpdatedEvents.onNext(map)

        //THEN
        assertTrue(currenciesListViewModel.isEmpty.get())
    }

    @Test
    fun `GIVEN a currencies map WHEN pass to view model THEN items updated`() {

        //GIVEN
        val map = mockMap_1_BASE_EUR()
        val map2 = mockMap_2_BASE_EUR()

        //WHEN
        currenciesListViewModel.startObserve()
        currenciesUpdatedEvents.onNext(map)

        getItems(currenciesListViewModel.items)
            .onEach { assertEquals(map[it.currency.name], it.currency.exchangeRate) }

        currenciesUpdatedEvents.onNext(map2)

        //THEN
        getItems(currenciesListViewModel.items)
            .onEach { assertEquals(map2[it.currency.name], it.currency.exchangeRate) }

    }

    @Test
    fun `GIVEN a currencies map WHEN update amount THEN items updated`() {

        //GIVEN
        val map = mockMap_1_BASE_EUR()

        //WHEN
        currenciesListViewModel.startObserve()
        currenciesUpdatedEvents.onNext(map)
        val newExchangeAmount = 50.0
        userEvents.onNext(CurrenciesListViewModel.Event.OnExchangeAmountChanged(newExchangeAmount))

        //THEN
        getItems(currenciesListViewModel.items)
            .onEach { assertEquals(it.exchangeAmount, newExchangeAmount, newExchangeAmount) }
    }

    @Test
    fun `GIVEN a currencies map WHEN update base currency THEN items updated`() {

        //GIVEN
        val map = mockMap_1_BASE_EUR()
        val map2 = mockMap_1_BASE_AUD()

        //WHEN
        currenciesListViewModel.startObserve()
        currenciesUpdatedEvents.onNext(map)
        val newBaseCurrency = getItems(currenciesListViewModel.items)[1]
        userEvents.onNext(CurrenciesListViewModel.Event.OnBaseCurrencyChanged(newBaseCurrency))
        currenciesUpdatedEvents.onNext(map2)

        //THEN
        val items = getItems(currenciesListViewModel.items)
        assertEquals(items[0].currency.exchangeRate, 1.0, 100000.0)
        assertEquals(items[0].currency.name, "AUD")
        items.onEach { assertEquals(map2[it.currency.name], it.currency.exchangeRate) }
    }

    private fun getItems(
        field: ObservableField<MutableList<CurrencyItemViewModel>>
    ): MutableList<CurrencyItemViewModel> = field.get()
        ?: throw AssertionFailedError("Items are empty")

}