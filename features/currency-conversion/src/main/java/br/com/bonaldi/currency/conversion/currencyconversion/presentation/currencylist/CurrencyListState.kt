package br.com.bonaldi.currency.conversion.currencyconversion.presentation.currencylist

import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates

data class CurrencyListState(
    val currencyList: List<Currency> = emptyList(),
    val ratesList: List<Rates> = emptyList(),
    val isLoading: Boolean = false
)