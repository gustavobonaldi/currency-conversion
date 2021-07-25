package br.com.bonaldi.currency.conversion.presentation.conversions

import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO

data class CurrencyConversionVO(
    var currencyFrom: CurrencyDTO? = null,
    var currencyTo: CurrencyDTO? = null
) {
}