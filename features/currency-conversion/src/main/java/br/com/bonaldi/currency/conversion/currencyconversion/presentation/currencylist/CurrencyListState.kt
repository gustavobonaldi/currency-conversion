package br.com.bonaldi.currency.conversion.currencyconversion.presentation.currencylist

import br.com.bonaldi.currency.conversion.core.database.model.CurrencyModel
import br.com.bonaldi.currency.conversion.core.database.model.RatesModel

data class CurrencyListState(
    val currencyList: List<CurrencyModel> = emptyList(),
    val ratesList: List<RatesModel> = emptyList(),
    val isLoading: Boolean = false
)