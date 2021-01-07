package br.com.btg.btgchallenge.domain

import br.com.btg.btgchallenge.data.repository.CurrencyLayerRepositoryImpl
import br.com.btg.btgchallenge.api.api.config.Resource

class CurrencyLayerUseCaseImpl(private val currencyLayerRepository: CurrencyLayerRepositoryImpl): CurrencyLayerUseCase{
    override suspend fun getCurrencies(): Resource<Any> {
        return currencyLayerRepository.getCurrencies()
    }

    override suspend fun getRealTimeRates(): Resource<Any> {
        return currencyLayerRepository.getRealTimeRates()
    }
}