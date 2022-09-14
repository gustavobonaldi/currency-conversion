package br.com.bonaldi.currency.conversion.core.model.mappers

import br.com.bonaldi.currency.conversion.core.database.model.CurrencyModel
import br.com.bonaldi.currency.conversion.core.database.model.RatesModel
import br.com.bonaldi.currency.conversion.core.model.currency.ExchangeRateDataDTO
import br.com.bonaldi.currency.conversion.core.model.currency.SupportedCurrenciesDTO

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