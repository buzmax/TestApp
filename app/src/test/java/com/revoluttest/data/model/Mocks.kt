package com.revoluttest.data.model

import com.squareup.moshi.Moshi

const val RESPONSE_1_BASE_EUR =
    "{\"base\":\"EUR\",\"date\":\"2018-09-06\",\"rates\":{\"AUD\":1.6239,\"BGN\":1.978,\"BRL\":4.814}}"
const val RESPONSE_2_BASE_EUR =
    "{\"base\":\"EUR\",\"date\":\"2018-09-06\",\"rates\":{\"AUD\":1.6166,\"BGN\":1.956,\"BRL\":4.814}}"
const val RESPONSE_1_BASE_AUD =
    "{\"base\":\"AUD\",\"date\":\"2018-09-06\",\"rates\":{\"EUR\":1.234,\"BGN\":1.513,\"BRL\":4.13}}"

internal fun mockResponse_1_BASE_EUR() =
    Moshi.Builder().build().adapter(Response::class.java).fromJson(RESPONSE_1_BASE_EUR)!!

internal fun mockResponse_2_BASE_EUR() =
    Moshi.Builder().build().adapter(Response::class.java).fromJson(RESPONSE_2_BASE_EUR)!!

internal fun mockResponse_1_BASE_AUD() =
    Moshi.Builder().build().adapter(Response::class.java).fromJson(RESPONSE_1_BASE_AUD)!!