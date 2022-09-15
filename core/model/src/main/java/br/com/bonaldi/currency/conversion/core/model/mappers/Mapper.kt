package br.com.bonaldi.currency.conversion.core.model.mappers

import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import br.com.bonaldi.currency.conversion.core.model.currency.ExchangeRateDataDTO
import br.com.bonaldi.currency.conversion.core.model.currency.SupportedCurrenciesDTO

object Mapper {
    fun toCurrencyModel(supportedCurrencies: SupportedCurrenciesDTO?): List<Currency> {
        return mutableListOf<Currency>().apply {
            supportedCurrencies?.currencies?.map { entry ->
                add(
                    Currency(
                        code = entry.key,
                        country = entry.value
                    )
                )
            }
        }.toList()
    }

    fun toRatesModel(rates:ExchangeRateDataDTO?): List<Rates>{
        return mutableListOf<Rates>().apply {
            rates?.quotes?.map { entry ->
                add(
                    Rates(
                        currencyCode = entry.key,
                        currencyValueInDollar = entry.value
                    )
                )
            }
        }.toList()
    }
}