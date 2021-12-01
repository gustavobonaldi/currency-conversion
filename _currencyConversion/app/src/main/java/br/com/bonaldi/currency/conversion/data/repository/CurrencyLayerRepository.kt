package br.com.bonaldi.currency.conversion.data.repository

import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import kotlinx.coroutines.flow.Flow

interface CurrencyLayerRepository {
    suspend fun selectRecentlyUsedCurrencies(): List<CurrencyModel>
    suspend fun updateRecentlyUsedCurrency(currencyCode: String, recentlyUsed: Boolean)
    suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean)

    suspend fun updateCurrencyList(onResult: suspend (Flow<Resource<List<CurrencyModel>>>) -> Unit)
    suspend fun updateCurrencyRateList(onResult: suspend (Flow<Resource<List<RatesModel>>>) -> Unit)
}