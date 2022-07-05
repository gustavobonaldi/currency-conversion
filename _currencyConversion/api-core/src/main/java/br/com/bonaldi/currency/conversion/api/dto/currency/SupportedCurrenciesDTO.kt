package br.com.bonaldi.currency.conversion.api.dto.currency

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel

data class SupportedCurrenciesDTO(
    var currencies: Map<String, String>?
): ApiResponse()