package br.com.bonaldi.currency.conversion.domain

import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepositoryImpl
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyLayerUseCaseImpl(private val currencyLayerRepository: CurrencyLayerRepository): CurrencyLayerUseCase {

    override suspend fun getCurrencies(showLoading: (Boolean) -> Unit): Resource<out Any> {
        return withContext(Dispatchers.IO) {
            currencyLayerRepository.getCurrencies(showLoading)
        }
    }

    override suspend fun getRealTimeRates(showLoading: (Boolean) -> Unit): Resource<out Any> {
        return withContext(Dispatchers.IO) {
            currencyLayerRepository.getRealTimeRates(showLoading)
        }
    }

    override fun getCurrenciesLiveData() = currencyLayerRepository.getCurrenciesLiveData()
    override fun getRealtimeRatesLiveData() = currencyLayerRepository.getRealtimeRatesLiveData()
}