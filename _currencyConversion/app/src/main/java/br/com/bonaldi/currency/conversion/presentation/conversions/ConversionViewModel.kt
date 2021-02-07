package br.com.bonaldi.currency.conversion.presentation.conversions

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCaseImpl
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.Currencies
import br.com.bonaldi.currency.conversion.api.dto.currency.Quotes
import br.com.bonaldi.currency.conversion.presentation.BaseViewModel
import br.com.bonaldi.currency.conversion.presentation.currencylist.Conversions
import java.text.NumberFormat
import java.util.*

class ConversionViewModel(
    private val currencyLayerUseCase: CurrencyLayerUseCaseImpl
    ) : BaseViewModel(), Conversions.List{

    private val _realTimeRates = MutableLiveData<Quotes>()
    val realTimeRatesLiveData: LiveData<Quotes> = _realTimeRates

    private val _convertedValue = MutableLiveData<String>()
    val convertedValueLiveData: LiveData<String> = _convertedValue

    override fun updateRealtimeRates() {
        launch {
            currencyLayerUseCase.getRealTimeRates()
        }
    }

    override fun addRealtimeRatesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (Quotes) -> Unit
    ){
        currencyLayerUseCase.getRealtimeRatesLiveData()?.observe(lifecycleOwner, Observer {quotes ->
            _realTimeRates.postValue(quotes)
            onSuccess.invoke(quotes)
        })
    }

    override fun getConversionFromTo(currencyFrom: Pair<String, String>?, currencyTo: Pair<String, String>?, valueToConvert: Double, event: (ErrorDTO) -> Unit?)
    {
        if(currencyFrom == null || currencyTo == null || currencyFrom.first.isEmpty() || currencyTo.first.isEmpty()){
            event.invoke(ErrorDTO(R.string.currencies_not_selected))
        }
        else {
            val quoteFrom =
               realTimeRatesLiveData.value?.quotes?.filterKeys { it.toUpperCase() == "USD" + currencyFrom.first.toUpperCase() }?.entries?.firstOrNull()
            val quoteTo =
                realTimeRatesLiveData.value?.quotes?.filterKeys { it.toUpperCase() == "USD" + currencyTo.first.toUpperCase() }?.entries?.firstOrNull()
            if (quoteFrom != null && quoteTo != null) {
                val valueInDolar = valueToConvert / quoteFrom.value
                val conversion = valueInDolar * quoteTo.value
                val conversionString = NumberFormat.getCurrencyInstance(Locale.US).format(conversion)
                _convertedValue.postValue(conversionString)
            } else {
                _convertedValue.postValue("$0.0000")
            }
        }
    }
}