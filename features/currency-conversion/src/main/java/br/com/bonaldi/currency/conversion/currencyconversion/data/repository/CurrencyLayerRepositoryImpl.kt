package br.com.bonaldi.currency.conversion.currencyconversion.data.repository

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.core.database.cache.CurrencyDataStore
import br.com.bonaldi.currency.conversion.core.database.model.CurrencyModel
import br.com.bonaldi.currency.conversion.core.database.model.RatesModel
import br.com.bonaldi.currency.conversion.core.model.mappers.Mapper.toCurrencyModel
import br.com.bonaldi.currency.conversion.core.model.mappers.Mapper.toRatesModel
import br.com.bonaldi.currency.conversion.core.database.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.core.database.room.dao.CurrencyRateDao
import br.com.bonaldi.currency.conversion.api.utils.BaseRepository
import br.com.bonaldi.currency.conversion.currencyconversion.data.api.CurrencyLayerServices
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyLayerRepositoryImpl @Inject constructor (
    private val currencyLayerApi: CurrencyLayerServices,
    private val currencyDao: CurrencyDao,
    private val currencyRateDao: CurrencyRateDao,
    private val currencyDataStore: CurrencyDataStore,
) : BaseRepository(), CurrencyLayerRepository {

    override suspend fun updateCurrencyList(): Flow<ResponseResource<List<CurrencyModel>>> {
        return request(
            apiRequest = currencyLayerApi::getCurrencies,
            localMapper = ::toCurrencyModel,
            localRequest = currencyDao::getCurrenciesFlow,
            updateLocal = { supportedCurrencies ->
                currencyDao.setCurrencyList(supportedCurrencies)
            }
        )
    }

    override suspend fun updateCurrencyRateList(): Flow<ResponseResource<List<RatesModel>>> {
        return request(
            apiRequest = currencyLayerApi::getRealTimeRates,
            localMapper = ::toRatesModel,
            localRequest = currencyRateDao::getRatesFlow,
            updateLocal = { quotes ->
                currencyRateDao.insertAll(quotes)
            }
        )
    }

    override suspend fun selectRecentlyUsedCurrencies(): List<CurrencyModel> {
        return currencyDao.selectRecentlyUsedCurrencies()
    }

    override suspend fun updateRecentlyUsedCurrency(currencyCode: String, recentlyUsed: Boolean) {
        val timeInMillis = if (recentlyUsed) System.currentTimeMillis() else 0L
        currencyDao.updateRecentlyUsedCurrency(currencyCode, recentlyUsed, timeInMillis)
    }

    override suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean) {
        currencyDao.updateFavoriteCurrency(currencyCode, isFavorite)
    }

    override fun getCurrencyListFlow(): Flow<List<CurrencyModel>>{
        return currencyDao.getCurrenciesFlow()
    }

    override fun getCurrentRatesFlow():  Flow<List<RatesModel>>{
        return currencyRateDao.getRatesFlow()
    }
}