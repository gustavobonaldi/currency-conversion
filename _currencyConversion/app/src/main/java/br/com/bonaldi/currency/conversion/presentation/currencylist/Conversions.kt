package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.LifecycleOwner
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.presentation.conversions.CurrencyConversionVO

interface Conversions {

    fun updateCurrencies()
    fun updateRealtimeRates()

    fun addRealtimeRatesObserver(
        lifecycleOwner: LifecycleOwner,
        onResult: (List<RatesDTO>?) -> Unit)
    fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (List<CurrencyDTO>?) -> Unit)

    fun getConversionFromTo(
        currencyConversionVO: CurrencyConversionVO,
        valueToConvert: Double,
        onSuccess: (String?) -> Unit,
        onError: (ErrorDTO) -> Unit?)

    fun updateCurrencyRecentlyUsed(currencyCode: String)
    fun updateCurrencyFavorite(currency: CurrencyDTO)




}