package br.com.bonaldi.currency.conversion.api.utils

import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO2
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO

object DataUtils {
    fun mapCurrencyResponseToModel(currencyMap: Map<String, String>?): List<CurrencyDTO2> {
        return mutableListOf<CurrencyDTO2>().apply {
            currencyMap?.map { entry ->
                add(
                    CurrencyDTO2(
                        currencyCode = entry.key,
                        currencyCountry = entry.value
                    )
                )
            }
        }
    }

    fun mapRatesResponseToModel(ratesMap: Map<String, Double>?): List<RatesDTO>{
        return mutableListOf<RatesDTO>().apply {
            ratesMap?.map { entry ->
                add(
                    RatesDTO(
                        currencyCode = entry.key,
                        currencyValueInDollar = entry.value
                    )
                )
            }
        }
    }
}