package br.com.bonaldi.currency.conversion.core.model.currency

import br.com.bonaldi.currency.conversion.core.common.data.model.ApiResponse

data class ExchangeRateDataDTO(
    var quotes: HashMap<String, Double>?
): ApiResponse()