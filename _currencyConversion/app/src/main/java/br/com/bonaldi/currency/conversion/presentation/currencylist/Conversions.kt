package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.LifecycleOwner
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO2
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO

interface Conversions {

    fun updateCurrencies()
    fun updateRealtimeRates()

    fun addRealtimeRatesObserver(
        lifecycleOwner: LifecycleOwner)
    fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (List<CurrencyDTO2>?) -> Unit)

    fun getConversionFromTo(
        currencyFrom: CurrencyDTO2?,
        currencyTo: CurrencyDTO2?,
        valueToConvert: Double,
        onSuccess: (String?) -> Unit,
        onError: (ErrorDTO) -> Unit?)





}