package br.com.bonaldi.currency.conversion.currencyconversion.presentation

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency.CurrencyType
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import br.com.bonaldi.currency.conversion.currencyconversion.data.enums.ConversionErrorEnum
import br.com.bonaldi.currency.conversion.currencyconversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.conversions.ConversionUtils
import br.com.bonaldi.currency.conversion.utils.extensions.empty
import br.com.bonaldi.currency.conversion.utils.extensions.safeLet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val currencyLayerUseCase: CurrencyLayerUseCase
): ViewModel(), Conversions {

    companion object {
        private const val DOLLAR_CURRENCY_CODE = "USD"
    }

    private val _currencyListState = MutableStateFlow<CurrencyListState>(CurrencyListState.Success())
    val currencyListState = _currencyListState

    private val _conversionState = MutableStateFlow(ConversionState())
    val conversionState = _conversionState

    private val _currencyRatesData = MutableStateFlow(CurrencyRatesData())

    private val _conversionEventFlow = MutableSharedFlow<ConversionUIEvent?>()
    val conversionEventFlow = _conversionEventFlow.asSharedFlow()

    private val _searchOnCurrencyListState = MutableStateFlow(SearchOnCurrencyListState())
    val searchOnCurrencyListState = _searchOnCurrencyListState

    init {
        updateCurrencies()
        updateRealtimeRates()
    }

    override fun updateRealtimeRates() {
        viewModelScope.launch {
            currencyLayerUseCase.updateCurrencyRateList().collectLatest { result ->
                when (result) {
                    is ResponseResource.Success -> {
                        _currencyRatesData.value =
                            _currencyRatesData.value.copy(ratesList = result.data.orEmpty())
                    }
                    is ResponseResource.Error -> {
                        result.error.message?.let {
                            _conversionEventFlow.emit(ConversionUIEvent.SnackBarError(it))
                        }
                    }
                    is ResponseResource.Loading -> {
                        _currencyRatesData.value = _currencyRatesData.value.copy(isLoading = true)
                    }
                    else -> {}
                }
            }
        }
    }

    override fun updateCurrencies() {
        viewModelScope.launch {
            currencyLayerUseCase.updateCurrencyList().collectLatest { result ->
                when (result) {
                    is ResponseResource.Success<List<Currency>> -> {
                        _currencyListState.value = CurrencyListState.Success(result.data.orEmpty())
                    }
                    is ResponseResource.Error -> {
                        result.error.message?.let {
                            _currencyListState.value = CurrencyListState.Error(it)
                        }
                    }
                    is ResponseResource.Loading -> {
                        _currencyListState.value = CurrencyListState.Loading
                    }
                    else -> {}
                }
            }
        }
    }

    override fun performConversion(valueToConvert: Double) {
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
                        ConversionErrorEnum.CURRENCIES_NOT_SELECTED_ERROR.message
                    )
                }
            } catch (ex: Exception) {
                when (ex) {
                    is NumberFormatException -> updateSnackBarState(
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
                convertedValue = null
            )
        }
    }

    override fun updateCurrencyFavorite(currency: Currency) {
        viewModelScope.launch {
            currencyLayerUseCase.updateFavoriteCurrency(currency.code, !currency.isFavorite)
        }
    }

    override fun searchOnCurrencyList(searchText: String) {
        (currencyListState.value as? CurrencyListState.Success)?.currencyList?.let { currencyList ->
            val searchTextClean = searchText.clearText()
            _searchOnCurrencyListState.value = _searchOnCurrencyListState.value.copy(
                searchText = searchText,
                resultList = currencyList.filter { it.code.clearText().contains(searchTextClean) || it.country?.clearText()?.contains(searchTextClean) ?: false }
            )
        }
    }

    private fun updateSnackBarState(message: Int) {
        viewModelScope.launch {
            _conversionEventFlow.emit(
                ConversionUIEvent.SnackBarError(message)
            )
        }
    }

    private fun searchCurrencyRatesInList(currency: Currency): Rates? {
        return _currencyRatesData.value.ratesList.firstOrNull { it.currencyCode == "$DOLLAR_CURRENCY_CODE${currency.code}" }
    }

    private fun updateHistory(origin: Currency, destiny: Currency){
        viewModelScope.launch {
            currencyLayerUseCase.updateConversionHistory(origin, destiny)
        }
    }

    sealed class ConversionUIEvent {
        data class SnackBarError(val message: Any) : ConversionUIEvent()
        data class ShowConvertedValue(val convertedValue: String) : ConversionUIEvent()
    }

    data class ConversionState(
        val currencyFrom: Currency? = null,
        val currencyTo: Currency? = null,
        val convertedValue: String? = null,
        val isLoading: Boolean = false
    )

    sealed class CurrencyListState {
        data class Success(val currencyList: List<Currency> = emptyList()): CurrencyListState()
        data class Error(val message: String): CurrencyListState()
        object Loading: CurrencyListState()
    }

    data class SearchOnCurrencyListState (
        val searchText: String = String.empty(),
        val resultList: List<Currency> = listOf()
    ) {
        val isSearching: Boolean
            get() = searchText.isNotBlank()

    }

    data class CurrencyRatesData(
        val ratesList: List<Rates> = emptyList(),
        val isLoading: Boolean = false
    )

    private fun String.clearText() = this.lowercase(Locale.getDefault())
}