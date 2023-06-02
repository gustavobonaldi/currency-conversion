package br.com.bonaldi.currency.conversion.currencyconversion.presentation.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.conversions.ConversionScreen
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.currencylist.CurrencyListScreen

@Composable
fun ConversionApp(
    viewModel: ConversionViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    ConversionNavHost(navController = navController, viewModel = viewModel)
}

@Composable
fun ConversionNavHost(
    navController: NavHostController,
    viewModel: ConversionViewModel
) {
    val activity = (LocalContext.current as Activity)
    NavHost(navController = navController, startDestination = Screen.ConversionScreen.route) {
        composable(route = Screen.ConversionScreen.route) {
            ConversionScreen(navController, viewModel)
        }
        composable(
            route = Screen.CurrencyListScreen.route + "/{currencyType}",
            arguments = listOf(navArgument("currencyType") {
                type = NavType.StringType
                defaultValue = Currency.CurrencyType.FROM.name
            })
        ) { entry ->
            val currencyTypeParam = entry.arguments?.getString("currencyType")?.let {
                NavType.EnumType(Currency.CurrencyType::class.java).parseValue(it)
            }
            CurrencyListScreen(
                navController = navController,
                viewModel = viewModel,
                currencyType = currencyTypeParam ?: Currency.CurrencyType.FROM
            )
        }
    }
}