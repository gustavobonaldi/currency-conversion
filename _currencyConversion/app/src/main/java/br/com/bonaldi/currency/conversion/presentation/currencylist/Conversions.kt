package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.LifecycleOwner
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO

interface Conversions {

    interface List{
        fun updateRealtimeRates()
        fun addRealtimeRatesObserver(
            lifecycleOwner: LifecycleOwner
        )
        fun getConversionFromTo(
            currencyFrom: Pair<String, String>?,
            currencyTo: Pair<String, String>?,
            valueToConvert: Double,
            onSuccess: (String) -> Unit,
            onError: (ErrorDTO) -> Unit?)
    }

    fun updateCurrencies()
    fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (CurrenciesDTO) -> Unit
    )

}