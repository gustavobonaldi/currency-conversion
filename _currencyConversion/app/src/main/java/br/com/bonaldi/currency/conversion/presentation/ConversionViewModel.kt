package br.com.bonaldi.currency.conversion.presentation

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesDTO
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.presentation.conversions.ConversionUtils
import br.com.bonaldi.currency.conversion.presentation.conversions.Currency
import br.com.bonaldi.currency.conversion.presentation.currencylist.Conversions
import br.com.bonaldi.currency.conversion.presentation.extensions.empty
import java.text.NumberFormat
import br.com.bonaldi.currency.conversion.presentation.conversions.Currency.CurrencyType
import java.util.*

class ConversionViewModel(private val currencyLayerUseCase: CurrencyLayerUseCase) : BaseViewModel(), Conversions{

    var currencyFrom = Currency(Pair(String.empty(), String.empty()), CurrencyType.FROM)
    var currencyTo = Currency(Pair(String.empty(), String.empty()), CurrencyType.TO)

    private val _realTimeRates = MutableLiveData<QuotesDTO>()
    private val realTimeRatesLiveData: LiveData<QuotesDTO> = _realTimeRates

    private val _getCurrencies = MutableLiveData<Resource<Any>>()
    val getCurrencies: LiveData<Resource<Any>> = _getCurrencies

    override fun updateRealtimeRates() {
        launch {
            currencyLayerUseCase.getRealTimeRates(showLoading = {
                //show loader
            })
        }
    }

    override fun addRealtimeRatesObserver(
        lifecycleOwner: LifecycleOwner){
        currencyLayerUseCase.getRealtimeRatesLiveData()?.observe(lifecycleOwner, Observer {quotes ->
            _realTimeRates.postValue(quotes)
        })
    }

    override fun getConversionFromTo(currencyFrom: Pair<String, String>?,
                                     currencyTo: Pair<String, String>?,
                                     valueToConvert: Double,
                                     onSuccess: (String) -> Unit,
                                     onError: (ErrorDTO) -> Unit?)
    {
        if(currencyFrom == null || currencyTo == null || currencyFrom.first.isEmpty() || currencyTo.first.isEmpty()){
            onError.invoke(ErrorDTO(errorMessage =  R.string.currencies_not_selected))
        }
        else {
            val quoteFrom =
               realTimeRatesLiveData.value?.quotes?.filterKeys { it.toUpperCase().equals("USD" + currencyFrom.first.toUpperCase())}?.entries?.firstOrNull()?.toPair()
            val quoteTo =
                realTimeRatesLiveData.value?.quotes?.filterKeys { it.toUpperCase().equals("USD" + currencyTo.first.toUpperCase()) }?.entries?.firstOrNull()?.toPair()

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
            currencyLayerUseCase.getCurrencies(showLoading = {
                //show loader
            })
        }
    }

    override fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (CurrenciesDTO) -> Unit
    ){
        currencyLayerUseCase.getCurrenciesLiveData()?.observe(lifecycleOwner, Observer {currencies ->
            onSuccess(currencies)
        })
    }
}