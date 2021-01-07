package br.com.btg.btgchallenge.data.repository;


import android.content.Context
import br.com.btg.btgchallenge.data.api.CurrencyLayerServices
import br.com.btg.btgchallenge.api.api.config.Resource
import br.com.btg.btgchallenge.api.api.config.ResponseHandler
import br.com.btg.btgchallenge.api.dto.ApiResponse
import br.com.btg.btgchallenge.api.dto.currency.Currencies
import br.com.btg.btgchallenge.api.dto.currency.Quotes
import java.lang.Exception

class CurrencyLayerRepositoryImpl(
    private val currencyLayerApi: CurrencyLayerServices,
    private val responseHandler: ResponseHandler,
    private val currencyRepositoryLocal: CurrencyRepositoryLocal,
    private val context: Context
): CurrencyLayerRepository {

    override suspend fun getCurrencies(): Resource<Any> {
        return try {
            val currencies = currencyRepositoryLocal.getCurrencies()
            var response = ApiResponse<Any>()
            if (currencies == null || currencies.currencies.isEmpty()) {
                response = currencyLayerApi.getCurrencies()
                currencyRepositoryLocal.insertCurrencies(Currencies(1, response.currencies, response.timestamp))
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
            val quotes = currencyRepositoryLocal.getQuotes()
            var response = ApiResponse<Any>()
            if (quotes == null || quotes.quotes.isEmpty()) {
                response = currencyLayerApi.getRealTimeRates()
                currencyRepositoryLocal.insertQuotes(Quotes(1, response.quotes, response.timestamp))
            } else {
                response.quotes = quotes.quotes
            }
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}