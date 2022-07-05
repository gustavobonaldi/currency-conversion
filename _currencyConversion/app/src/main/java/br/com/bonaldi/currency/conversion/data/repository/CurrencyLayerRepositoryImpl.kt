package br.com.bonaldi.currency.conversion.data.repository

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.api.cache.CurrencyDataStore
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.exception.ApiException
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.api.model.mappers.Mapper.toCurrencyModel
import br.com.bonaldi.currency.conversion.api.model.mappers.Mapper.toRatesModel
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyRateDao
import br.com.bonaldi.currency.conversion.api.utils.BaseRepository
import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class CurrencyLayerRepositoryImpl(
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