package br.com.bonaldi.currency.conversion.presentation.conversions

import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.presentation.extensions.Zero

class ConversionUtils{
    companion object {
        fun convertValue(
            quoteFrom: RatesModel?,
            quoteTo: RatesModel?,
            valueToConvert: Double
        ): Double {
            return when {
                quoteFrom != null && quoteTo != null -> {
                    val valueInDollar = valueToConvert / quoteFrom.currencyValueInDollar
                    valueInDollar * quoteTo.currencyValueInDollar
                }
                else -> {
                    Double.Zero()
                }
            }
        }
    }
}