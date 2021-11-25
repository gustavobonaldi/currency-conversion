package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.LifecycleOwner
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.presentation.conversions.CurrencyConversionVO

interface Conversions {

    fun updateCurrencies()
    fun updateRealtimeRates()

    fun addRealtimeRatesObserver(
        lifecycleOwner: LifecycleOwner,
        onResult: (List<RatesModel>?) -> Unit)
    fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (List<CurrencyModel>?) -> Unit)

    fun getConversionFromTo(
        currencyConversionVO: CurrencyConversionVO,
        valueToConvert: Double,
        onSuccess: (String?) -> Unit,
        onError: (ErrorDTO) -> Unit?)

    fun updateCurrencyRecentlyUsed(currencyCode: String)
    fun updateCurrencyFavorite(currency: CurrencyModel)




}