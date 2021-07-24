package br.com.bonaldi.currency.conversion.presentation

import android.content.Context
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.presentation.conversions.ConversionUtils
import br.com.bonaldi.currency.conversion.presentation.currencylist.Conversions
import br.com.bonaldi.currency.conversion.presentation.extensions.empty
import java.text.NumberFormat
import java.util.*

class ConversionViewModel(
    private val context: Context,
    private val currencyLayerUseCase: CurrencyLayerUseCase
) : BaseViewModel(), Conversions {

    var currencyFrom = CurrencyDTO(String.empty())
    var currencyTo = CurrencyDTO(String.empty(), String.empty(), CurrencyDTO.CurrencyType.TO)

    private val _realTimeRates = MutableLiveData<List<RatesDTO>>()
    private val realTimeRatesLiveData: LiveData<List<RatesDTO>> = _realTimeRates

    private val _getCurrencies = MutableLiveData<List<CurrencyDTO>>()
    val getCurrencies: LiveData<List<CurrencyDTO>> = _getCurrencies

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
        lifecycleOwner: LifecycleOwner){
        currencyLayerUseCase.getCurrencyRateListLiveData()?.observe(lifecycleOwner, Observer {quotes ->
            _realTimeRates.postValue(quotes)
        })
    }

    override fun getConversionFromTo(
        currencyFrom: CurrencyDTO?,
        currencyTo: CurrencyDTO?,
        valueToConvert: Double,
        onSuccess: (String?) -> Unit,
        onError: (ErrorDTO) -> Unit?
    ) {

        if (currencyFrom == null || currencyTo == null || currencyFrom.currencyCode.isEmpty() || currencyTo.currencyCode.isEmpty()) {
            onError.invoke(ErrorDTO(info = context.getString(R.string.currencies_not_selected)))
        } else {
            val quoteFrom = realTimeRatesLiveData.value?.firstOrNull { it.currencyCode == "USD" + currencyFrom.currencyCode }
            val quoteTo = realTimeRatesLiveData.value?.firstOrNull { it.currencyCode == "USD" + currencyTo.currencyCode }

            ConversionUtils.convertValue(
                quoteFrom,
                quoteTo,
                valueToConvert
            ).let {
                onSuccess.invoke(NumberFormat.getCurrencyInstance(Locale.US).format(it))
            }
        }
    }

    override fun updateCurrencies() {
        launch {
            currencyLayerUseCase.updateCurrencyList(
                shouldShowLoading = {

                },
                onError = {

                },
                onSuccess = {

                }
            )
        }
    }

    override fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (List<CurrencyDTO>?) -> Unit
    ){
        currencyLayerUseCase.getCurrencyListLiveData()?.observe(lifecycleOwner, Observer {currencies ->
            onSuccess(currencies)
        })
    }
}