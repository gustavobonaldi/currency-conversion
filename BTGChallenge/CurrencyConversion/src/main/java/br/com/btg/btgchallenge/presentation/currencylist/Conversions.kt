package br.com.btg.btgchallenge.presentation.currencylist

import br.com.btg.btgchallenge.api.dto.ErrorDTO

interface Conversions {

    interface List{
        fun getRealtimeRates()
        fun getConversionFromTo(
            currencyFrom: Pair<String, String>?,
            currencyTo: Pair<String, String>?,
            valueToConvert: Double,
            event: (ErrorDTO) -> Unit?)
    }

    fun getCurrencies()

}