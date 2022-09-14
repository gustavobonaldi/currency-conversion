package br.com.bonaldi.currency.conversion.presentation

import android.content.Context
import br.com.bonaldi.currency.conversion.core.database.model.CurrencyModel

interface Conversions {
    fun updateCurrencies()
    fun updateCurrencyFavorite(currency: CurrencyModel)
    fun updateCurrencyRecentlyUsed(currency: CurrencyModel, currencyType: CurrencyModel.CurrencyType)
    fun updateRealtimeRates()
    fun performConversion(
        context: Context,
        valueToConvert: Double)
}