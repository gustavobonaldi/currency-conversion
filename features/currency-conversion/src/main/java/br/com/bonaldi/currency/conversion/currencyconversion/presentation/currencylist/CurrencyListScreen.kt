package br.com.bonaldi.currency.conversion.currencyconversion.presentation.currencylist

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.ConversionViewModel.*
import br.com.bonaldi.currency.conversion.utils.customcomponents.baseTypography
import br.com.bonaldi.currency.conversion.utils.extensions.empty
import br.com.bonaldi.currency.conversion.utils.extensions.getFlagDrawableResource

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CurrencyListScreen(
    navController: NavController,
    viewModel: ConversionViewModel,
    currencyType: Currency.CurrencyType = Currency.CurrencyType.FROM
) {
    val currencyListSate by viewModel.currencyListState.collectAsState()
    val searchOnCurrencyListState by viewModel.searchOnCurrencyListState.collectAsState()
    val shouldShowCurrencyList =
        !searchOnCurrencyListState.isSearching || (searchOnCurrencyListState.isSearching && searchOnCurrencyListState.resultList.isNotEmpty())
    val currencyList = when {
        searchOnCurrencyListState.isSearching -> searchOnCurrencyListState.resultList
        else -> (currencyListSate as? CurrencyListState.Success)?.currencyList.orEmpty()
    }
    var searchBarText by remember {
        mutableStateOf(String.empty())
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 12.dp)
    ) {
        when (currencyListSate) {
            is CurrencyListState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    (currencyListSate as? CurrencyListState.Error)?.message,
                    Toast.LENGTH_LONG
                ).show()
            }

            CurrencyListState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Black
                    )
                }
            }

            is CurrencyListState.Success -> {
                SearchBarUI(
                    searchText = searchBarText,
                    placeholderText = "Search for a currency",
                    onSearchTextChanged = { newText ->
                        searchBarText = newText
                        viewModel.searchOnCurrencyList(searchBarText)
                    },
                    onClearClick = {
                        searchBarText = String.empty()
                        viewModel.searchOnCurrencyList(String.empty())
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    matchesFound = shouldShowCurrencyList,
                    results = {
                        LazyColumn(modifier = Modifier) {
                            val firstRecentUseItem = currencyList.firstOrNull { it.isRecentUse }
                            val firstCurrencyItem = currencyList.firstOrNull { !it.isRecentUse }

                            items(currencyList, key = { it.code }) { currency ->
                                when (currency.code) {
                                    firstRecentUseItem?.code -> {
                                        TitleWithCurrencyHeader(
                                            title = "Recently Used",
                                            currency = currency,
                                            onClick = { selectedCurrency ->
                                                onSelectCurrency(
                                                    viewModel = viewModel,
                                                    navController = navController,
                                                    selectedCurrency = selectedCurrency,
                                                    currencyType = currencyType
                                                )
                                            }
                                        )
                                    }

                                    firstCurrencyItem?.code -> {
                                        TitleWithCurrencyHeader(
                                            title = "Currency List",
                                            currency = currency,
                                            onClick = { selectedCurrency ->
                                                onSelectCurrency(
                                                    viewModel = viewModel,
                                                    navController = navController,
                                                    selectedCurrency = selectedCurrency,
                                                    currencyType = currencyType
                                                )
                                            }
                                        )
                                    }
                                    else -> {
                                        CurrencyListItem(
                                            currency = currency,
                                            onClick = { selectedCurrency ->
                                                onSelectCurrency(
                                                    viewModel = viewModel,
                                                    navController = navController,
                                                    selectedCurrency = selectedCurrency,
                                                    currencyType = currencyType
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}

@Composable
fun TitleWithCurrencyHeader(title: String, currency: Currency, onClick: (Currency) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            text = title,
            style = baseTypography.bodyLarge
        )
        CurrencyListItem(currency = currency, onClick = onClick)
    }
}

@Composable
fun CurrencyListItem(currency: Currency, onClick: (Currency) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .clickable {
                onClick(currency)
            }
    ) {
        Image(
            modifier = Modifier
                .size(24.dp, 24.dp)
                .clip(CircleShape),
            painter = painterResource(
                id = getFlagDrawableResource(
                    LocalContext.current,
                    currency.code
                )
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp),
            text = currency.toString(),
            style = baseTypography.bodyMedium,
            color = Color.Black
        )
    }
}

fun onSelectCurrency(
    viewModel: ConversionViewModel,
    navController: NavController,
    selectedCurrency: Currency,
    currencyType: Currency.CurrencyType
) {
    viewModel.updateCurrencyRecentlyUsed(
        selectedCurrency,
        currencyType
    )
    navController.popBackStack()
}