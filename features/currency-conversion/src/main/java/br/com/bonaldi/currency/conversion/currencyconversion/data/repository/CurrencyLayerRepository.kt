package br.com.bonaldi.currency.conversion.currencyconversion.data.repository

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import kotlinx.coroutines.flow.Flow

interface CurrencyLayerRepository {
    suspend fun selectRecentlyUsedCurrencies(): List<Currency>
    suspend fun updateRecentlyUsedCurrency(currencyCode: String, recentlyUsed: Boolean)
    suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean)
    suspend fun updateConversionHistory(origin: Currency, destiny: Currency)

    suspend fun updateCurrencyList(): Flow<ResponseResource<List<Currency>>>
    suspend fun updateCurrencyRateList(): Flow<ResponseResource<List<Rates>>>

    fun getCurrencyListFlow(): Flow<List<Currency>>
    fun getCurrentRatesFlow():  Flow<List<Rates>>
}