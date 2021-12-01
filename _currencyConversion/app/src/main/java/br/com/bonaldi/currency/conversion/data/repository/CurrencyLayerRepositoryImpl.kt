package br.com.bonaldi.currency.conversion.data.repository

import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.cache.CurrencyDataStore
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyRateDao
import br.com.bonaldi.currency.conversion.api.utils.BaseRepository
import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CurrencyLayerRepositoryImpl(
    private val currencyLayerApi: CurrencyLayerServices,
    private val currencyDao: CurrencyDao,
    private val currencyRateDao: CurrencyRateDao,
    private val currencyDataStore: CurrencyDataStore,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseRepository(defaultDispatcher), CurrencyLayerRepository {

    override suspend fun updateCurrencyList(onResult: suspend (Flow<Resource<List<CurrencyModel>>>) -> Unit) {
        return createRequest(
            request = {
                val supportedCurrencies = currencyLayerApi.getCurrencies()
                supportedCurrencies.toCurrencyModel(supportedCurrencies.currencies)
            },
            localRequest = {
                currencyDao.getCurrenciesFlow()
            },
            updateLocal = { list ->
                withContext(defaultDispatcher) {
                    currencyDao.setCurrencyList(list)
                }
            },
            onResult = {
                onResult.invoke(it)
            }
        )
    }

    override suspend fun updateCurrencyRateList(onResult: suspend (Flow<Resource<List<RatesModel>>>) -> Unit) {
        return createRequest(
            request = {
                val quotes = currencyLayerApi.getRealTimeRates()
                quotes.toRatesModel(quotes.quotes)
            },
            localRequest = {
                currencyRateDao.getRatesFlow()
            },
            updateLocal = {
                withContext(defaultDispatcher) {
                    currencyRateDao.insertAll(it)
                }
            },
            onResult = {
                onResult.invoke(it)
            }
        )
    }

    override suspend fun selectRecentlyUsedCurrencies(): List<CurrencyModel> {
        return currencyDao.selectRecentlyUsedCurrencies()
    }

    override suspend fun updateRecentlyUsedCurrency(currencyCode: String, recentlyUsed: Boolean) {
        withContext(defaultDispatcher) {
            val timeInMillis = if (recentlyUsed) System.currentTimeMillis() else 0L
            currencyDao.updateRecentlyUsedCurrency(currencyCode, recentlyUsed, timeInMillis)
        }
    }

    override suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean) {
        currencyDao.updateFavoriteCurrency(currencyCode, isFavorite)
    }
}