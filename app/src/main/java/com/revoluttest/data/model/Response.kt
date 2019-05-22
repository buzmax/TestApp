package com.revoluttest.data.model

import com.squareup.moshi.Json

internal data class Response(
    @Json(name = "base") val base: String = "",
    @Json(name = "date") val date: String = "",
    @Json(name = "rates") val rates: Map<String, Double> = emptyMap()
)