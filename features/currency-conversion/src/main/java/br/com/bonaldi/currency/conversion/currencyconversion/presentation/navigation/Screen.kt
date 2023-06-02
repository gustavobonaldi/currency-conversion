package br.com.bonaldi.currency.conversion.currencyconversion.presentation.navigation

sealed class Screen(val route: String) {
    object ConversionScreen: Screen("conversion_screen")
    object CurrencyListScreen: Screen("currency_list_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}