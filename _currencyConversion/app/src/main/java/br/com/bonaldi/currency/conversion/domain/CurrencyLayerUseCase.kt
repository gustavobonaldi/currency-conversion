package br.com.bonaldi.currency.conversion.domain

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.core.database.model.CurrencyModel
import br.com.bonaldi.currency.conversion.core.database.model.RatesModel
import kotlinx.coroutines.flow.Flow

interface CurrencyLayerUseCase {
    suspend fun updateRecentlyUsedCurrency(currencyCode: String)
    suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean)

    suspend fun updateCurrencyList(): Flow<ResponseResource<List<CurrencyModel>>>
    suspend fun updateCurrencyRateList(): Flow<ResponseResource<List<RatesModel>>>

    fun getCurrencyListFlow(): Flow<List<CurrencyModel>>
    fun getCurrentRatesFlow():  Flow<List<RatesModel>>
}