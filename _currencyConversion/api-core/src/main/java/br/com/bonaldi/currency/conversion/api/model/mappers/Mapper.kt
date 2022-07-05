package br.com.bonaldi.currency.conversion.api.model.mappers

import br.com.bonaldi.currency.conversion.api.dto.currency.ExchangeRateDataDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.SupportedCurrenciesDTO
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.RatesModel

object Mapper {

    fun toCurrencyModel(supportedCurrencies: SupportedCurrenciesDTO?): List<CurrencyModel> {
        return mutableListOf<CurrencyModel>().apply {
            supportedCurrencies?.currencies?.map { entry ->
                add(
                    CurrencyModel(
                        currencyCode = entry.key,
                        currencyCountry = entry.value
                    )
                )
            }
        }.toList()
    }

    fun toRatesModel(rates:ExchangeRateDataDTO?): List<RatesModel>{
        return mutableListOf<RatesModel>().apply {
            rates?.quotes?.map { entry ->
                add(
                    RatesModel(
                        currencyCode = entry.key,
                        currencyValueInDollar = entry.value
                    )
                )
            }
        }.toList()
    }
}