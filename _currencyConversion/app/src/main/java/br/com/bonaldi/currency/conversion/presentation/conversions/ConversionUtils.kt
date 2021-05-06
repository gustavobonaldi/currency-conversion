package br.com.bonaldi.currency.conversion.presentation.conversions

import br.com.bonaldi.currency.conversion.presentation.extensions.Zero

class ConversionUtils{
    companion object {
        fun convertValue(
            quoteFrom: Pair<String, Double>?,
            quoteTo: Pair<String, Double>?,
            valueToConvert: Double
        ): Double {
            if (quoteFrom != null && quoteTo != null) {
                val valueInDolar = valueToConvert / quoteFrom.second
                val convertedValue = valueInDolar * quoteTo.second
                return convertedValue
            } else {
                return Double.Zero()
            }
        }
    }
}