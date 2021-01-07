package br.com.btg.btgchallenge.data.repository

import br.com.btg.btgchallenge.network.api.config.Resource

interface CurrencyLayerRepository {
    suspend fun getCurrencies(): Resource<Any>
    suspend fun getRealTimeRates(): Resource<Any>
}