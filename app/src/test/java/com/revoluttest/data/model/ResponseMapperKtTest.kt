package com.revoluttest.data.model

import org.junit.Assert
import org.junit.Test

class ResponseMapperKtTest {

    @Test
    fun `GIVEN an currencies response WHEN mapped to a map THEN a map should be returned`() {

        //GIVEN
        val response = mockResponse_1_BASE_EUR()

        //WHEN
        val actual = response.toCurrenciesMap()

        //THEN
        Assert.assertEquals(actual["EUR"], 1.0)
        response.rates.forEach { Assert.assertEquals(actual[it.key], it.value) }
    }
}