package br.com.bonaldi.currency.conversion.currencyconversion.presentation.conversions

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.bonaldi.currency.conversion.currencyconversion.R
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.utils.composable.RoundedNumber
import br.com.bonaldi.currency.conversion.utils.customcomponents.CardCurrency
import br.com.bonaldi.currency.conversion.utils.customcomponents.baseTypography
import br.com.bonaldi.currency.conversion.utils.customcomponents.fontFamily
import java.text.NumberFormat
import java.util.Locale
import br.com.bonaldi.currency.conversion.utils.R as UtilsR
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency.CurrencyType
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.ConversionViewModel.ConversionUIEvent
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.navigation.Screen
import br.com.bonaldi.currency.conversion.utils.extensions.empty
import br.com.bonaldi.currency.conversion.utils.extensions.getFlagDrawableResource
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ConversionScreen(
    navController: NavController = rememberNavController(),
    viewModel: ConversionViewModel = hiltViewModel(),
) {
    val startGradientColor = colorResource(UtilsR.color.startGradient)
    val centerGradientColor = colorResource(UtilsR.color.centerGradient)
    val endGradientColor = colorResource(UtilsR.color.endGradient)
    
    SetStatusBar(color = startGradientColor)

    val conversionUiState by viewModel.conversionEventFlow.collectAsState(initial = null)
    val conversionData by viewModel.conversionState.collectAsState()

    var convertedValue by remember {
        mutableStateOf("$0,00")
    }

    var currencyFrom by remember {
        mutableStateOf<Currency?>(null)
    }

    var currencyTo by remember {
        mutableStateOf<Currency?>(null)
    }

    convertedValue = conversionData.convertedValue ?: "$0,00"
    currencyFrom = conversionData.currencyFrom
    currencyTo = conversionData.currencyTo

    when(conversionUiState) {
        is ConversionUIEvent.ShowConvertedValue -> {
            convertedValue = (conversionUiState as? ConversionUIEvent.ShowConvertedValue)?.convertedValue ?: "$0,00"
        }
        is ConversionUIEvent.SnackBarError -> {
            (conversionUiState as? ConversionUIEvent.SnackBarError)?.message?.let { message ->
                val toastText = when (message) {
                    is String -> message
                    is Int -> stringResource(id = message)
                    else -> null
                }
                toastText?.let {
                    Toast.makeText(LocalContext.current, it, Toast.LENGTH_LONG).show()
                }
            }
        }
        else -> {}
    }

    val colorStops = arrayOf(
        0.0f to startGradientColor,
        0.2f to centerGradientColor,
        1f to endGradientColor
    )
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                Brush.linearGradient(
                    colorStops = colorStops,
                    start = Offset.Infinite,
                    end = Offset.Zero
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                )
        ) {
            CardCurrency(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.CurrencyListScreen.withArgs(CurrencyType.FROM.name))
                },
                roundedNumber = "1",
                descriptionText = currencyFrom?.toString()
                    ?: stringResource(id = R.string.select_currency),
                selectedCountryImageRes = getFlagDrawableResource(
                    context = LocalContext.current,
                    currencyCode = currencyFrom?.code
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            CardCurrency(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.CurrencyListScreen.withArgs(CurrencyType.TO.name))
                },
                roundedNumber = "2",
                descriptionText = currencyTo?.toString()
                    ?: stringResource(id = R.string.select_another_currency),
                selectedCountryImageRes = getFlagDrawableResource(
                    context = LocalContext.current,
                    currencyCode = currencyTo?.code
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedNumber(Modifier, "4")
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    text = convertedValue,
                    color = Color.White,
                    style = baseTypography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedNumber(Modifier, "3")
                ConversionTextField(
                    shouldRequestFocus = currencyFrom != null && currencyTo != null
                ) { value ->
                    viewModel.performConversion(
                        value
                    )
                }
            }
        }
    }
}

@Composable
fun SetStatusBar(color: Color) {
    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = color,
            darkIcons = useDarkIcons
        )

        // setStatusBarColor() and setNavigationBarColor() also exist

        onDispose {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionTextField(
    shouldRequestFocus: Boolean,
    onPerformConversion: (value: Double) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var conversionTextField by remember {
        mutableStateOf(TextFieldValue("$0,00"))
    }
    var shouldBlockChangeValue: Boolean = false
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    TextField(
        value = conversionTextField,
        onValueChange = { newText ->
            if (!shouldBlockChangeValue) {
                shouldBlockChangeValue = true
                val cleanString = newText.text.filter { it.isDigit() }
                if (cleanString.isNotEmpty()) {
                    val value = (cleanString.toDoubleOrNull() ?: 0.0) / 100.0
                    onPerformConversion(value)
                    numberFormat.format((value)).let { formattedNumber ->
                        conversionTextField = newText.copy(
                            text = formattedNumber,
                            selection = TextRange(formattedNumber.length)
                        )
                    }
                } else {
                    conversionTextField = newText
                }
            } else {
                shouldBlockChangeValue = false
            }
        },
        maxLines = 2,
        textStyle = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            fontSize = 24.sp,
            background = Color.Transparent,
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(start = 10.dp)
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            containerColor = Color.Transparent,
            placeholderColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            focusedTrailingIconColor = Color.Transparent,
            focusedSupportingTextColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedSupportingTextColor = Color.Transparent,
            unfocusedTrailingIconColor = Color.Transparent
        ),
        placeholder = {
            Text("Type a value")
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )

    if (shouldRequestFocus) {
        LaunchedEffect(Unit) {
            conversionTextField = conversionTextField.copy(text = String.empty())
            focusRequester.requestFocus()
        }
    }
}