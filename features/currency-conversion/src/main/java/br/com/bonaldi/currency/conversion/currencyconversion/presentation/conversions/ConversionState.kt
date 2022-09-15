package br.com.bonaldi.currency.conversion.currencyconversion.presentation.conversions

import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency

data class ConversionState(
    val currencyFrom: Currency? = null,
    val currencyTo: Currency? = null,
    val convertedValue: String? = null,
    val isLoading: Boolean = false
)