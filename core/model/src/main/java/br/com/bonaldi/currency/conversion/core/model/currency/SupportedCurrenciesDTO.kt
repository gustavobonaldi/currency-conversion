package br.com.bonaldi.currency.conversion.core.model.currency

import br.com.bonaldi.currency.conversion.core.common.data.model.ApiResponse

data class SupportedCurrenciesDTO(
    var currencies: Map<String, String>?
): ApiResponse()