package br.com.bonaldi.currency.conversion.data.repository

import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesResponseDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesResponseDTO

interface CurrencyLayerRepository {
    suspend fun getCurrencies(showLoading: (Boolean) -> Unit): Resource<out CurrenciesResponseDTO>
    suspend fun getRealTimeRates(showLoading: (Boolean) -> Unit): Resource<out QuotesResponseDTO>
    fun getCurrenciesLiveData(): LiveData<CurrenciesDTO>?
    fun getRealtimeRatesLiveData(): LiveData<QuotesDTO>?
}