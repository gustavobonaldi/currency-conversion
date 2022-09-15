package br.com.bonaldi.currency.conversion.currencyconversion.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency.CurrencyType
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import br.com.bonaldi.currency.conversion.currencyconversion.data.enums.ConversionErrorEnum
import br.com.bonaldi.currency.conversion.currencyconversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.conversions.ConversionState
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.conversions.ConversionUtils
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.currencylist.CurrencyListState
import br.com.bonaldi.currency.conversion.utils.extensions.safeLet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val currencyLayerUseCase: CurrencyLayerUseCase
): ViewModel(), Conversions {

    companion object {
        private const val DOLLAR_CURRENCY_CODE = "USD"
    }

    private val _conversionState = MutableStateFlow(ConversionState())
    val conversionState = _conversionState

    private val _currencyListState = MutableStateFlow(CurrencyListState())
    val currencyListState = _currencyListState

    private val _conversionEventFlow = MutableSharedFlow<ConversionUIEvent>()
    val conversionEventFlow = _conversionEventFlow.asSharedFlow()

    override fun updateRealtimeRates() {
        viewModelScope.launch {
            currencyLayerUseCase.updateCurrencyRateList().collectLatest { result ->
                when (result) {
                    is ResponseResource.Success -> {
                        _currencyListState.value =
                            _currencyListState.value.copy(ratesList = result.data.orEmpty())
                    }
                    is ResponseResource.Error -> {
                        result.error.message?.let {
                            _conversionEventFlow.emit(ConversionUIEvent.SnackBarError(it))
                        }
                    }
                    is ResponseResource.Loading -> {
                        _conversionState.value =
                            conversionState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    override fun updateCurrencies() {
        viewModelScope.launch {
            currencyLayerUseCase.updateCurrencyList().collectLatest { result ->
                when (result) {
                    is ResponseResource.Success<List<Currency>> -> {
                        _currencyListState.value =
                            _currencyListState.value.copy(currencyList = result.data.orEmpty())
                    }
                    is ResponseResource.Error -> {
                        result.error.message?.let {
                            _conversionEventFlow.emit(ConversionUIEvent.SnackBarError(it))
                        }
                    }
                    is ResponseResource.Loading -> {
                        _conversionState.value =
                            conversionState.value.copy(isLoading = result.isLoading)
                    }
                }
            }
        }
    }

    override fun performConversion(
        context: Context,
        valueToConvert: Double,
    ) {
        viewModelScope.launch {
            try {
                safeLet(
                    _conversionState.value.currencyFrom,
                    _conversionState.value.currencyTo
                ) { currencyFrom, currencyTo ->

                    val convertedValue = ConversionUtils.convertValue(
                        searchCurrencyRatesInList(currencyFrom),
                        searchCurrencyRatesInList(currencyTo),
                        valueToConvert
                    )

                    _conversionState.value = _conversionState.value.copy(
                        currencyFrom = _conversionState.value.currencyFrom,
                        currencyTo = _conversionState.value.currencyTo,
                        convertedValue = NumberFormat.getCurrencyInstance(Locale.US)
                            .format(convertedValue)
                    )

                    _conversionEventFlow.emit(ConversionUIEvent.ShowConvertedValue(_conversionState.value.convertedValue.orEmpty()))
                    updateHistory(currencyFrom, currencyTo)
                } ?: run {
                    updateSnackBarState(
                        context,
                        ConversionErrorEnum.CURRENCIES_NOT_SELECTED_ERROR.message
                    )
                }
            } catch (ex: Exception) {
                when (ex) {
                    is NumberFormatException -> updateSnackBarState(
                        context,
                        ConversionErrorEnum.INVALID_VALUE_TYPED_ERROR.message
                    )
                }
            }
        }
    }

    override fun updateCurrencyRecentlyUsed(
        currency: Currency,
        currencyType: CurrencyType
    ) {
        viewModelScope.launch {
            currencyLayerUseCase.updateRecentlyUsedCurrency(currency.code)
            _conversionState.value = _conversionState.value.copy(
                currencyFrom = if (currencyType == CurrencyType.FROM) currency else _conversionState.value.currencyFrom,
                currencyTo = if (currencyType == CurrencyType.TO) currency else _conversionState.value.currencyTo,
            )
            _conversionEventFlow.emit(
                ConversionUIEvent.SelectClickedCurrency(
                    currency,
                    currencyType
                )
            )
        }
    }

    override fun updateCurrencyFavorite(currency: Currency) {
        viewModelScope.launch {
            currencyLayerUseCase.updateFavoriteCurrency(currency.code, !currency.isFavorite)
        }
    }

    private fun updateSnackBarState(context: Context, message: Int) {
        viewModelScope.launch {
            _conversionEventFlow.emit(
                ConversionUIEvent.SnackBarError(
                    context.resources.getString(message)
                )
            )
        }
    }

    private fun searchCurrencyRatesInList(currency: Currency): Rates? {
        return _currencyListState.value.ratesList.firstOrNull { it.currencyCode == "$DOLLAR_CURRENCY_CODE${currency.code}" }
    }

    private fun updateHistory(origin: Currency, destiny: Currency){
        viewModelScope.launch {
            currencyLayerUseCase.updateConversionHistory(origin, destiny)
        }
    }

    sealed class ConversionUIEvent {
        data class SnackBarError(val message: String) : ConversionUIEvent()
        data class SelectClickedCurrency(
            val currency: Currency,
            val currencyType: CurrencyType
        ) : ConversionUIEvent()

        data class ShowConvertedValue(val convertedValue: String) : ConversionUIEvent()
    }
}