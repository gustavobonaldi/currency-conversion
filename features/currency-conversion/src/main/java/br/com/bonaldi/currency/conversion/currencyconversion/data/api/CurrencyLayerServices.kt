package br.com.bonaldi.currency.conversion.currencyconversion.data.api;

import br.com.bonaldi.currency.conversion.core.model.currency.ExchangeRateDataDTO
import br.com.bonaldi.currency.conversion.core.model.currency.SupportedCurrenciesDTO
import retrofit2.http.GET

interface CurrencyLayerServices {
    @GET("/list")
    suspend fun getCurrencies(): SupportedCurrenciesDTO

    @GET("/live")
    suspend fun getRealTimeRates(): ExchangeRateDataDTO
}