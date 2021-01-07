package br.com.btg.btgchallenge.presentation.conversions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.btg.btgchallenge.R
import br.com.btg.btgchallenge.network.api.config.Resource
import br.com.btg.btgchallenge.domain.CurrencyLayerUseCaseImpl
import br.com.btg.btgchallenge.network.dto.ErrorDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class ConversionViewModel(val currencyLayerUseCase: CurrencyLayerUseCaseImpl) : ViewModel(){

    private val _realTimeRates = MutableLiveData<Resource<Any>>()
    val realTimeRatesLiveData: LiveData<Resource<Any>> = _realTimeRates
    fun getRealtimeRates() {
        viewModelScope.launch(context = Dispatchers.IO)
        {
            _realTimeRates.postValue(Resource.loading())
            _realTimeRates.postValue(currencyLayerUseCase.getRealTimeRates())
        }
    }


    private val _convertedValue = MutableLiveData<String>()
    val convertedValueLiveData: LiveData<String> = _convertedValue
    fun getConversionFromTo(currencyFrom: Pair<String, String>?, currencyTo: Pair<String, String>?, valueToConvert: Double, event: (ErrorDTO) -> Unit?)
    {
        if(currencyFrom == null || currencyTo == null || currencyFrom.first.isEmpty() || currencyTo.first.isEmpty()){
            event.invoke(ErrorDTO(R.string.currencies_not_selected))
        }
        else {
            val quoteFrom =
                _realTimeRates.value?.data?.quotes?.filterKeys { it.toUpperCase() == "USD" + currencyFrom.first.toUpperCase() }?.entries?.firstOrNull()
            val quoteTo =
                _realTimeRates.value?.data?.quotes?.filterKeys { it.toUpperCase() == "USD" + currencyTo.first.toUpperCase() }?.entries?.firstOrNull()
            if (quoteFrom != null && quoteTo != null) {
                val valueOrigin = quoteFrom.value
                val valueDestiny = quoteTo.value
                var valueInDolar = valueToConvert / valueOrigin

                var conversion = valueInDolar * valueDestiny
                val conversionString =
                    NumberFormat.getCurrencyInstance(Locale.US).format(conversion)
                _convertedValue.postValue(conversionString)
            } else {
                _convertedValue.postValue("$0.0000")
            }
        }
    }


}