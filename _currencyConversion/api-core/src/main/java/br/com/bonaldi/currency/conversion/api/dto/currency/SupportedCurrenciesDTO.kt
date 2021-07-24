package br.com.bonaldi.currency.conversion.api.dto.currency

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse

data class SupportedCurrenciesDTO(
    var currencies: Map<String, String>?
): ApiResponse(){
}