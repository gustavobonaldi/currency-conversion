package br.com.bonaldi.currency.conversion.data.repository;


import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.api.config.ResponseHandler
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesResponseDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesResponseDTO
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyLayerRepositoryImpl(
    private val currencyLayerApi: CurrencyLayerServices,
    override var responseHandler: ResponseHandler,
    private val currencyDao: CurrencyDao
): BaseRepository(responseHandler), CurrencyLayerRepository {

    override fun getCurrenciesLiveData() = currencyDao.getCurrenciesLiveData()
    override fun getRealtimeRatesLiveData() = currencyDao.getQuotesLiveData()

    override suspend fun getCurrencies(showLoading: (Boolean) -> Unit): Resource<out CurrenciesResponseDTO> {
        return withContext(Dispatchers.IO){
            createRequest(showLoading, request = {
                currencyLayerApi.getCurrencies().let { response ->
                    currencyDao.insertCurrencies(CurrenciesDTO(1, response.currencies, response.timestamp))
                    responseHandler.handleSuccess(response)
                }
            })
        }
    }

    override suspend fun getRealTimeRates(showLoading: (Boolean) -> Unit): Resource<out QuotesResponseDTO> {
        return withContext(Dispatchers.IO) {
            createRequest(showLoading, request = {
                currencyLayerApi.getRealTimeRates().let { response ->
                    currencyDao.insertQuotes(QuotesDTO(1, response.quotes, response.timestamp))
                    responseHandler.handleSuccess(response)
                }
            })
        }
    }
}