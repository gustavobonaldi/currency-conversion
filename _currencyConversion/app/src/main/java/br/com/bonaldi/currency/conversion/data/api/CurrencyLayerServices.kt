package br.com.bonaldi.currency.conversion.data.api;

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import retrofit2.http.*

interface CurrencyLayerServices {
    @GET("/list")
    suspend fun getCurrencies(): ApiResponse<Any>

    @GET("/live")
    suspend fun getRealTimeRates(): ApiResponse<Any>
}