package br.com.bonaldi.currency.conversion.presentation.conversions

data class Currency(
    val currency: Pair<String, String>,
    val type: CurrencyType = CurrencyType.FROM
) {
    constructor() : this(Pair("", ""), CurrencyType.FROM)

    enum class CurrencyType {
        FROM,
        TO
    }
}

