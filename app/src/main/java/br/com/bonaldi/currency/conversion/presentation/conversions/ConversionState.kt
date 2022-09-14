package br.com.bonaldi.currency.conversion.presentation.conversions

import br.com.bonaldi.currency.conversion.core.database.model.CurrencyModel

data class ConversionState(
    val currencyFrom: CurrencyModel? = null,
    val currencyTo: CurrencyModel? = null,
    val convertedValue: String? = null,
    val isLoading: Boolean = false
)