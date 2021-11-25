package br.com.bonaldi.currency.conversion.data.repository

import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.model.RatesModel

interface CurrencyLayerRepository {
    suspend fun updateCurrencyList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<CurrencyModel>) -> Unit
    )

    suspend fun updateCurrencyRateList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<RatesModel>) -> Unit
    )

    suspend fun selectRecentlyUsedCurrencies(): List<CurrencyModel>
    suspend fun updateCurrencyRecentlyUsed(currencyCode: String, recentlyUsed: Boolean)
    suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean)

    fun getCurrencyListLiveData(): LiveData<List<CurrencyModel>?>
    fun getCurrencyRateListLiveData(): LiveData<List<RatesModel>?>
}