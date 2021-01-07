package br.com.btg.btgchallenge.presentation.conversions

data class Currency(
    val currency: Pair<String, String>,
    val type: CurrencyType
) {
    constructor() : this(Pair("", ""), CurrencyType.FROM)

    enum class CurrencyType {
        FROM,
        TO
    }
}

