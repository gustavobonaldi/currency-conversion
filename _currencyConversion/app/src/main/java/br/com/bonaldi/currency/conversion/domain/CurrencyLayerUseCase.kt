package br.com.bonaldi.currency.conversion.domain

import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesDTO

interface CurrencyLayerUseCase{
    suspend fun getCurrencies(showLoading: (Boolean) -> Unit): Resource<out Any>
    suspend fun getRealTimeRates(showLoading: (Boolean) -> Unit): Resource<out Any>
    fun getCurrenciesLiveData(): LiveData<CurrenciesDTO>?
    fun getRealtimeRatesLiveData(): LiveData<QuotesDTO>?
}