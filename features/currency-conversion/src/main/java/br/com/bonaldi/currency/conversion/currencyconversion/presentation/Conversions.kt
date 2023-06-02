package br.com.bonaldi.currency.conversion.currencyconversion.presentation

import android.content.Context
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency

interface Conversions {
    fun updateCurrencies()
    fun updateCurrencyFavorite(currency: Currency)
    fun updateCurrencyRecentlyUsed(currency: Currency, currencyType: Currency.CurrencyType)
    fun updateRealtimeRates()
    fun performConversion(valueToConvert: Double)
    fun searchOnCurrencyList(searchText: String)
}