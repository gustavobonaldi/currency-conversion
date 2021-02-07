package br.com.bonaldi.currency.conversion.domain

import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepositoryImpl
import br.com.bonaldi.currency.conversion.api.api.config.Resource

class CurrencyLayerUseCaseImpl(
    private val currencyLayerRepository: CurrencyLayerRepositoryImpl
    ): CurrencyLayerUseCase {

    override suspend fun getCurrencies(): Resource<Any> {
        return currencyLayerRepository.getCurrencies()
    }

    override suspend fun getRealTimeRates(): Resource<Any> {
        return currencyLayerRepository.getRealTimeRates()
    }

    override fun getCurrenciesLiveData() = currencyLayerRepository.getCurrenciesLiveData()
    override fun getRealtimeRatesLiveData() = currencyLayerRepository.getRealtimeRatesLiveData()
}