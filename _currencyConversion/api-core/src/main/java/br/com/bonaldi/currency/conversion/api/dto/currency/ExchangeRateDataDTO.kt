package br.com.bonaldi.currency.conversion.api.dto.currency

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse

data class ExchangeRateDataDTO(
    var quotes: HashMap<String, Double>?
): ApiResponse(){
}