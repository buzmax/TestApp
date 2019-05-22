package com.revoluttest.domain.model

data class Currency(
    val name: CharSequence,
    val exchangeRate: Double
) {
    companion object {
        val DEFAULT_CURRENCY = Currency("EUR", 1.0)
    }
}