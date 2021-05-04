package br.com.bonaldi.currency.conversion.api.dto.currency

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse

data class CurrenciesResponseDTO(
    var currencies: HashMap<String, String>?
): ApiResponse(){
}