package com.revoluttest.viewmodel

import androidx.databinding.ObservableField
import com.revoluttest.domain.model.Currency
import java.text.DecimalFormat

internal class CurrencyItemViewModel(
    var currency: Currency,
    var exchangeAmount: Double,
    var isBaseCurrency: Boolean,
    private val userInteractions: CurrencyListUserInteractions
) {
    val exchangedAmount = ObservableField<String>()
    private val decimalFormat = DecimalFormat.getNumberInstance()

    init {
        updateExchangedAmount()
        decimalFormat.maximumFractionDigits = 2
        decimalFormat.isGroupingUsed = false
    }

    val amountListener = { amount: CharSequence ->

        fun parseAmount(amount: CharSequence): Double =
            try {
                amount.toString().toDouble()
            } catch (exception: NumberFormatException) {
                0.0
            }

        if (isBaseCurrency) {
            val newExchangeAmount = parseAmount(amount)
            if (newExchangeAmount != exchangeAmount) {
                exchangedAmount.set(amount.toString())
                userInteractions.changeExchangeAmount(newExchangeAmount)
            }
        }
    }

    fun updateItem(updatedRate: Double, updatedExchangeAmount: Double) {
        updateExchangeAmount(updatedExchangeAmount)
        if (updatedRate <= 0.0 || currency.exchangeRate == updatedRate) return
        currency = currency.copy(exchangeRate = updatedRate)
        if (!isBaseCurrency) updateExchangedAmount()
    }

    fun updateExchangeAmount(amount: Double) {
        if (exchangeAmount == amount) return
        exchangeAmount = amount
        if (!isBaseCurrency) updateExchangedAmount()
    }

    fun onSelected() {
        if (!isBaseCurrency) userInteractions.changeBaseCurrency(this)
    }

    private fun updateExchangedAmount() {
        val newAmount = exchangeAmount * currency.exchangeRate
        exchangedAmount.set(if (newAmount <= 0.0) "" else decimalFormat.format(newAmount))
    }
}