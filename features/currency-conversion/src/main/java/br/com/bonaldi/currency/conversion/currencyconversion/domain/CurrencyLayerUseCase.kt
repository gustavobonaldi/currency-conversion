package br.com.bonaldi.currency.conversion.currencyconversion.domain

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import kotlinx.coroutines.flow.Flow

interface CurrencyLayerUseCase {
    suspend fun updateRecentlyUsedCurrency(currencyCode: String)
    suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean)

    suspend fun updateCurrencyList(): Flow<ResponseResource<List<Currency>>>
    suspend fun updateCurrencyRateList(): Flow<ResponseResource<List<Rates>>>
    suspend fun updateConversionHistory(origin: Currency, destiny: Currency)


    fun getCurrencyListFlow(): Flow<List<Currency>>
    fun getCurrentRatesFlow():  Flow<List<Rates>>
}