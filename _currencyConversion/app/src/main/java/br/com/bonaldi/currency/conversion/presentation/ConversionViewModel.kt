package br.com.bonaldi.currency.conversion.presentation

import android.content.Context
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel.CurrencyType
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.data.enums.ConversionErrorEnum
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.presentation.conversions.ConversionState
import br.com.bonaldi.currency.conversion.presentation.conversions.ConversionUtils
import br.com.bonaldi.currency.conversion.presentation.currencylist.CurrencyListState
import br.com.bonaldi.currency.conversion.utils.extensions.safeLet
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.*

class ConversionViewModel(
    private val currencyLayerUseCase: CurrencyLayerUseCase
) : BaseViewModel(), Conversions {

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
        launch {
            currencyLayerUseCase.updateCurrencyRateList { rateListFlow ->
                rateListFlow.collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _currencyListState.value =
                                _currencyListState.value.copy(ratesList = result.data.orEmpty())
                        }
                        is Resource.Error -> {
                            _conversionEventFlow.emit(ConversionUIEvent.SnackBarError(result.error.info))
                        }
                        is Resource.Loading -> {
                            _conversionState.value =
                                conversionState.value.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    override fun updateCurrencies() {
        launch {
            currencyLayerUseCase.updateCurrencyList { currencyFlow ->
                currencyFlow.collectLatest { result ->
                    when (result) {
                        is Resource.Success -> {
                            _currencyListState.value =
                                _currencyListState.value.copy(currencyList = result.data.orEmpty())
                        }
                        is Resource.Error -> {
                            _conversionEventFlow.emit(ConversionUIEvent.SnackBarError(result.error.info))
                        }
                        is Resource.Loading -> {
                            _conversionState.value =
                                conversionState.value.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    override fun performConversion(
        context: Context,
        valueToConvert: Double,
    ) {
        launch {
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
                        convertedValue = NumberFormat.getCurrencyInstance(Locale.US).format(convertedValue)
                    )

                    _conversionEventFlow.emit(ConversionUIEvent.ShowConvertedValue(_conversionState.value.convertedValue.orEmpty()))
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
        currency: CurrencyModel,
        currencyType: CurrencyType
    ) {
        launch {
            currencyLayerUseCase.updateRecentlyUsedCurrency(currency.currencyCode)
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

    override fun updateCurrencyFavorite(currency: CurrencyModel) {
        launch {
            currencyLayerUseCase.updateFavoriteCurrency(currency.currencyCode, !currency.isFavorite)
        }
    }

    private fun updateSnackBarState(context: Context, message: Int) {
        launch {
            _conversionEventFlow.emit(
                ConversionUIEvent.SnackBarError(
                    context.resources.getString(message)
                )
            )
        }
    }

    private fun searchCurrencyRatesInList(currency: CurrencyModel): RatesModel? {
        return _currencyListState.value.ratesList.firstOrNull { it.currencyCode == "$DOLLAR_CURRENCY_CODE${currency.currencyCode}" }
    }

    sealed class ConversionUIEvent {
        data class SnackBarError(val message: String) : ConversionUIEvent()
        data class SelectClickedCurrency(
            val currency: CurrencyModel,
            val currencyType: CurrencyType
        ) : ConversionUIEvent()

        data class ShowConvertedValue(val convertedValue: String) : ConversionUIEvent()
    }
}