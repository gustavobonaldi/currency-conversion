package br.com.bonaldi.currency.conversion.data.repository;


import br.com.bonaldi.currency.conversion.api.cache.CurrencyDataStore
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyRateDao
import br.com.bonaldi.currency.conversion.api.utils.DataUtils.mapCurrencyResponseToModel
import br.com.bonaldi.currency.conversion.api.utils.DataUtils.mapRatesResponseToModel
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
        onSuccess: (List<CurrencyDTO>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            createRequest(
                shouldShowLoading,
                onError,
                request = {
                    currencyLayerApi.getCurrencies()
                },
                onSuccess = { response ->
                    mapCurrencyResponseToModel(response.currencies).let { currencyList ->
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
        onSuccess: (List<RatesDTO>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            createRequest(
                shouldShowLoading,
                onError,
                request = {
                    currencyLayerApi.getRealTimeRates()
                },
                onSuccess = { response ->
                    mapRatesResponseToModel(response.quotes).let { currencyRateList ->
                        runOnBG {
                            currencyRateDao.insert(currencyRateList)
                        }
                        onSuccess.invoke(currencyRateList)
                    }
                }
            )
        }
    }

    override fun getCurrencyListLiveData() = currencyDao.getCurrenciesLiveData()
    override fun getCurrencyRateListLiveData() = currencyRateDao.getRatesLiveData()
}