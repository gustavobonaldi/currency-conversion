package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.LifecycleOwner
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.Currencies
import br.com.bonaldi.currency.conversion.api.dto.currency.Quotes

interface Conversions {

    interface List{
        fun updateRealtimeRates()
        fun addRealtimeRatesObserver(
            lifecycleOwner: LifecycleOwner,
            onSuccess: (Quotes) -> Unit
        )
        fun getConversionFromTo(
            currencyFrom: Pair<String, String>?,
            currencyTo: Pair<String, String>?,
            valueToConvert: Double,
            event: (ErrorDTO) -> Unit?)
    }

    fun updateCurrencies()
    fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (Currencies) -> Unit
    )

}