package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.LifecycleOwner
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO

interface Conversions {

    fun updateCurrencies()
    fun updateRealtimeRates()

    fun addRealtimeRatesObserver(
        lifecycleOwner: LifecycleOwner)
    fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (List<CurrencyDTO>?) -> Unit)

    fun getConversionFromTo(
        currencyFrom: CurrencyDTO?,
        currencyTo: CurrencyDTO?,
        valueToConvert: Double,
        onSuccess: (String?) -> Unit,
        onError: (ErrorDTO) -> Unit?)





}