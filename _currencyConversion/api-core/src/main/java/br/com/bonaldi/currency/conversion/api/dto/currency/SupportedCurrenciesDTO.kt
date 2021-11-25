package br.com.bonaldi.currency.conversion.api.dto.currency

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel

data class SupportedCurrenciesDTO(
    var currencies: Map<String, String>?
): ApiResponse(){

    fun toCurrencyModel(currencyMap: Map<String, String>?): List<CurrencyModel> {
        return mutableListOf<CurrencyModel>().apply {
            currencyMap?.map { entry ->
                add(
                    CurrencyModel(
                        currencyCode = entry.key,
                        currencyCountry = entry.value
                    )
                )
            }
        }.toList()
    }
}