package br.com.bonaldi.currency.conversion.presentation.currencylist

import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.RatesModel

data class CurrencyListState(
    val currencyList: List<CurrencyModel> = emptyList(),
    val ratesList: List<RatesModel> = emptyList(),
    val isLoading: Boolean = false
)