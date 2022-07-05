package br.com.bonaldi.currency.conversion.api.dto.currency

import br.com.bonaldi.currency.conversion.api.dto.ApiResponse
import br.com.bonaldi.currency.conversion.api.model.RatesModel

data class ExchangeRateDataDTO(
    var quotes: HashMap<String, Double>?
): ApiResponse()