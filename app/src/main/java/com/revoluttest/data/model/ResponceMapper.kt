package com.revoluttest.data.model

internal fun Response.toCurrenciesMap(): Map<String, Double> {
    val result = mutableMapOf<String, Double>()
    result[base] = 1.0
    result.putAll(rates)
    return result
}