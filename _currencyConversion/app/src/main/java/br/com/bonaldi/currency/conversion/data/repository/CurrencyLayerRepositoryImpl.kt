package br.com.bonaldi.currency.conversion.data.repository;


import br.com.bonaldi.currency.conversion.api.cache.CurrencyDataStore
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyRateDao
import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyLayerRepositoryImpl(
    private val currencyLayerApi: CurrencyLayerServices,
    private val currencyDao: CurrencyDao,
    private val currencyRateDao: CurrencyRateDao,
    private val currencyDataStore: CurrencyDataStore
) : BaseRepository(), CurrencyLayerRepository {

    override suspend fun updateCurrencyList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<CurrencyModel>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            createRequest(
                shouldShowLoading,
                onError,
                request = {
                    currencyLayerApi.getCurrencies()
                },
                onSuccess = { supportedCurrenciesResponse ->
                    supportedCurrenciesResponse.toCurrencyModel(supportedCurrenciesResponse.currencies)
                        .let { currencyList ->
                            runOnBG {
                                currencyDao.setCurrencyList(currencyList)
                            }
                            onSuccess.invoke(currencyList)
                        }
                })
        }
    }

    override suspend fun updateCurrencyRateList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<RatesModel>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            createRequest(
                shouldShowLoading,
                onError,
                request = {
                    currencyLayerApi.getRealTimeRates()
                },
                onSuccess = { exchangesRatesResponse ->
                    exchangesRatesResponse.toRatesModel(exchangesRatesResponse.quotes)
                        .let { currencyRateList ->
                            runOnBG {
                                currencyRateDao.insertAll(currencyRateList)
                            }
                            onSuccess.invoke(currencyRateList)
                        }
                }
            )
        }
    }

    override suspend fun selectRecentlyUsedCurrencies(): List<CurrencyModel> {
        return currencyDao.selectRecentlyUsedCurrencies()
    }

    override suspend fun updateCurrencyRecentlyUsed(currencyCode: String, recentlyUsed: Boolean) {
        val timeInMillis = if (recentlyUsed) System.currentTimeMillis() else 0L
        currencyDao.updateCurrencyRecentlyUsed(currencyCode, recentlyUsed, timeInMillis)
    }

    override suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean) {
        currencyDao.updateFavoriteCurrency(currencyCode, isFavorite)
    }

    override fun getCurrencyListLiveData() = currencyDao.getCurrenciesLiveData()
    override fun getCurrencyRateListLiveData() = currencyRateDao.getRatesLiveData()
}