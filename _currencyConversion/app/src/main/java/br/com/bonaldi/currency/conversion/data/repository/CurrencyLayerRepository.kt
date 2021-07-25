package br.com.bonaldi.currency.conversion.data.repository

import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO

interface CurrencyLayerRepository {
    suspend fun updateCurrencyList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<CurrencyDTO>) -> Unit)

    suspend fun updateCurrencyRateList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<RatesDTO>) -> Unit)

    suspend fun selectRecentlyUsedCurrencies(): List<CurrencyDTO>
    suspend fun updateCurrencyRecentlyUsed(currencyCode: String, recentlyUsed: Boolean)

    fun getCurrencyListLiveData(): LiveData<List<CurrencyDTO>?>
    fun getCurrencyRateListLiveData(): LiveData<List<RatesDTO>?>
}