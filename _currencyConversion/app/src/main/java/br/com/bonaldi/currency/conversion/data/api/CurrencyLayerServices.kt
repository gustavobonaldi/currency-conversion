package br.com.bonaldi.currency.conversion.data.api;

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesResponseDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesResponseDTO
import retrofit2.http.*

interface CurrencyLayerServices {
    @GET("/list")
    suspend fun getCurrencies(): CurrenciesResponseDTO

    @GET("/live")
    suspend fun getRealTimeRates(): QuotesResponseDTO
}