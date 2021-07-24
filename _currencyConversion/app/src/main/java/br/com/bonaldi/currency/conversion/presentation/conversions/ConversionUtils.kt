package br.com.bonaldi.currency.conversion.presentation.conversions

import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.presentation.extensions.Zero

class ConversionUtils{
    companion object {
        fun convertValue(
            quoteFrom: RatesDTO?,
            quoteTo: RatesDTO?,
            valueToConvert: Double
        ): Double {
            return if (quoteFrom != null && quoteTo != null) {
                val valueInDollar = valueToConvert / quoteFrom.currencyValueInDollar
                valueInDollar * quoteTo.currencyValueInDollar
            } else {
                Double.Zero()
            }
        }
    }
}