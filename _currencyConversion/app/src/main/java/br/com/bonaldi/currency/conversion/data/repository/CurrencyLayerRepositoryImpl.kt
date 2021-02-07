package br.com.bonaldi.currency.conversion.data.repository;


import android.content.Context
import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.api.config.ResponseHandler
import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.dto.currency.Currencies
import br.com.bonaldi.currency.conversion.api.dto.currency.Quotes
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import java.lang.Exception

class CurrencyLayerRepositoryImpl(
    private val currencyLayerApi: CurrencyLayerServices,
    private val responseHandler: ResponseHandler,
    private val currencyDao: CurrencyDao,
    private val context: Context
): CurrencyLayerRepository {

    override fun getCurrenciesLiveData() = currencyDao.getCurrenciesLiveData()
    override fun getRealtimeRatesLiveData() = currencyDao.getQuotesLiveData()

    override suspend fun getCurrencies(): Resource<Any> {
        return try {
            val currencies = currencyDao.getCurrencies()
            var response = ApiResponse<Any>()
            if (currencies == null || currencies.currencies.isEmpty()) {
                response = currencyLayerApi.getCurrencies()
                currencyDao.insertCurrencies(Currencies(1, response.currencies, response.timestamp))
            } else {
                response.currencies = currencies.currencies
            }
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    override suspend fun getRealTimeRates(): Resource<Any> {
        return try {
            val quotes = currencyDao.getQuotes()
            var response = ApiResponse<Any>()
            if (quotes == null || quotes.quotes.isEmpty()) {
                response = currencyLayerApi.getRealTimeRates()
                currencyDao.insertQuotes(Quotes(1, response.quotes, response.timestamp))
            } else {
                response.quotes = quotes.quotes
            }
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}