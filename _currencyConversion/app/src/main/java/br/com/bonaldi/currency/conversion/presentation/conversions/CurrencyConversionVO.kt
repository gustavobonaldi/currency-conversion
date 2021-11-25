package br.com.bonaldi.currency.conversion.presentation.conversions

import br.com.bonaldi.currency.conversion.api.model.CurrencyModel

data class CurrencyConversionVO(
    var currencyFrom: CurrencyModel? = null,
    var currencyTo: CurrencyModel? = null
)