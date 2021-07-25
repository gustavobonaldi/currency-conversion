package br.com.bonaldi.currency.conversion.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.presentation.conversions.ConversionUtils
import br.com.bonaldi.currency.conversion.presentation.conversions.CurrencyConversionVO
import br.com.bonaldi.currency.conversion.presentation.currencylist.Conversions
import br.com.bonaldi.currency.conversion.presentation.extensions.empty
import br.com.bonaldi.currency.conversion.presentation.extensions.safeLet
import java.text.NumberFormat
import java.util.*

class ConversionViewModel(
    private val context: Context,
    private val currencyLayerUseCase: CurrencyLayerUseCase
) : BaseViewModel(), Conversions {

    companion object {
        private const val DOLLAR_CURRENCY_CODE = "USD"
    }

    val currencyConversionVO = CurrencyConversionVO()
    private var currencyListMemory: List<CurrencyDTO>? = listOf()
    private val _realTimeRates = MutableLiveData<List<RatesDTO>>()
    private val realTimeRatesLiveData: LiveData<List<RatesDTO>> = _realTimeRates

    override fun updateRealtimeRates() {
        launch {
            currencyLayerUseCase.updateCurrencyRateList(
                shouldShowLoading = {
                    //show loader //
                },
                onError = {

                },
                onSuccess = {

                }
            )
        }
    }

    override fun addRealtimeRatesObserver(
        lifecycleOwner: LifecycleOwner,
        onResult: (List<RatesDTO>?) -> Unit
    ) {
        currencyLayerUseCase.getCurrencyRateListLiveData().observe(
            lifecycleOwner,
            Observer { list ->
                _realTimeRates.postValue(list)
                onResult.invoke(list)
            }
        )
    }

    override fun getConversionFromTo(
        currencyConversionVO: CurrencyConversionVO,
        valueToConvert: Double,
        onSuccess: (String?) -> Unit,
        onError: (ErrorDTO) -> Unit?
    ) {
        safeLet(
            currencyConversionVO.currencyFrom?.currencyCode,
            currencyConversionVO.currencyTo?.currencyCode
        ) { currencyCodeFrom, currencyCodeTo ->
            val ratesOfCurrencyFrom =
                realTimeRatesLiveData.value?.firstOrNull { it.currencyCode == "$DOLLAR_CURRENCY_CODE$currencyCodeFrom" }
            val ratesOfCurrencyTo =
                realTimeRatesLiveData.value?.firstOrNull { it.currencyCode == "$DOLLAR_CURRENCY_CODE$currencyCodeTo" }

            val convertedValue = ConversionUtils.convertValue(
                ratesOfCurrencyFrom,
                ratesOfCurrencyTo,
                valueToConvert
            )
            onSuccess.invoke(NumberFormat.getCurrencyInstance(Locale.US).format(convertedValue))
        } ?: run {
            onError.invoke(ErrorDTO(info = context.getString(R.string.currencies_not_selected)))
        }
    }

    override fun updateCurrencies() {
        launch {
            currencyLayerUseCase.updateCurrencyList(
                shouldShowLoading = {

                },
                onError = {

                },
                onSuccess = { list ->
                    Log.d("GUSTAVO", list.toString())
                }
            )
        }
    }

    override fun updateCurrencyRecentlyUsed(currencyCode: String) {
        launch {
            currencyLayerUseCase.updateCurrencyRecentlyUsed(currencyCode)
        }
    }

    override fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (List<CurrencyDTO>?) -> Unit
    ) {
        onSuccess.invoke(currencyListMemory)
        currencyLayerUseCase.getCurrencyListLiveData().observe(
            lifecycleOwner,
            Observer { currencies ->
                currencyListMemory = currencies
                onSuccess.invoke(currencies)
            }
        )

    }
}