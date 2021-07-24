package br.com.bonaldi.currency.conversion.data.api;

import br.com.bonaldi.currency.conversion.api.dto.currency.SupportedCurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.ExchangeRateDataDTO
import retrofit2.http.*

interface CurrencyLayerServices {
    @GET("/list")
    suspend fun getCurrencies(): SupportedCurrenciesDTO

    @GET("/live")
    suspend fun getRealTimeRates(): ExchangeRateDataDTO
}