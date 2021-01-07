package br.com.btg.btgchallenge.domain

import br.com.btg.btgchallenge.network.api.config.Resource

interface CurrencyLayerUseCase{
    suspend fun getCurrencies(): Resource<Any>
    suspend fun getRealTimeRates(): Resource<Any>
}