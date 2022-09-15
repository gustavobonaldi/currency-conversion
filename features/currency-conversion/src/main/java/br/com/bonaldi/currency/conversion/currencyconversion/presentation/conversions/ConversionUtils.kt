package br.com.bonaldi.currency.conversion.currencyconversion.presentation.conversions

import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import br.com.bonaldi.currency.conversion.utils.extensions.zero

class ConversionUtils{
    companion object {
        fun convertValue(
            quoteFrom: Rates?,
            quoteTo: Rates?,
            valueToConvert: Double
        ): Double {
            return when {
                quoteFrom != null && quoteTo != null -> {
                    val valueInDollar = valueToConvert / quoteFrom.currencyValueInDollar
                    valueInDollar * quoteTo.currencyValueInDollar
                }
                else -> {
                    Double.zero()
                }
            }
        }
    }
}