package com.revoluttest.data.api

import com.revoluttest.data.model.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://revolut.duckdns.org"

internal interface Api {
    @GET("/latest")
    fun getCurrencies(@Query("base") baseCurrency: CharSequence): Observable<Response>
}