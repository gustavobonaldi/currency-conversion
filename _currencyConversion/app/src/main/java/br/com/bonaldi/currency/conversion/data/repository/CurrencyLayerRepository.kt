package br.com.bonaldi.currency.conversion.data.repository

import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.dto.currency.Currencies
import br.com.bonaldi.currency.conversion.api.dto.currency.Quotes

interface CurrencyLayerRepository {
    suspend fun getCurrencies(): Resource<Any>
    suspend fun getRealTimeRates(): Resource<Any>
    fun getCurrenciesLiveData(): LiveData<Currencies>?
    fun getRealtimeRatesLiveData(): LiveData<Quotes>?
}