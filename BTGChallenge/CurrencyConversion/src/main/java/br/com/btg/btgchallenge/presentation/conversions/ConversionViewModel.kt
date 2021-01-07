package br.com.btg.btgchallenge.presentation.conversions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.btg.btgchallenge.R
import br.com.btg.btgchallenge.network.api.config.Resource
import br.com.btg.btgchallenge.network.api.config.Status
import br.com.btg.btgchallenge.data.repository.CurrencyLayerRepositoryImpl
import br.com.btg.btgchallenge.domain.CurrencyLayerUseCaseImpl
import br.com.btg.btgchallenge.network.dto.ErrorDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class ConversionViewModel(val currencyLayerUseCase: CurrencyLayerUseCaseImpl) : ViewModel(){

    val getRealtimeRates = MutableLiveData<Resource<Any>>()
    fun getRealtimeRates() {
        viewModelScope.launch(context = Dispatchers.IO)
        {
            getRealtimeRates.postValue(Resource.loading())
            val rates = currencyLayerUseCase.getRealTimeRates()
            when (rates.status) {
                Status.SUCCESS -> {
                }
            }
            getRealtimeRates.postValue(rates)
        }
    }

    val convertedValue = MutableLiveData<String>()
    fun getConversionFromTo(currencyFrom: Pair<String, String>?, currencyTo: Pair<String, String>?, valueToConvert: Double, event: (ErrorDTO) -> Unit?)
    {
        if(currencyFrom == null || currencyTo == null || currencyFrom.first.isEmpty() || currencyTo.first.isEmpty()){
            event.invoke(ErrorDTO(R.string.currencies_not_selected))
        }
        else {
            val quoteFrom =
                getRealtimeRates.value?.data?.quotes?.filterKeys { it.toUpperCase() == "USD" + currencyFrom.first.toUpperCase() }?.entries?.firstOrNull()
            val quoteTo =
                getRealtimeRates.value?.data?.quotes?.filterKeys { it.toUpperCase() == "USD" + currencyTo.first.toUpperCase() }?.entries?.firstOrNull()
            if (quoteFrom != null && quoteTo != null) {
                val valueOrigin = quoteFrom.value
                val valueDestiny = quoteTo.value
                var valueInDolar = valueToConvert / valueOrigin

                var conversion = valueInDolar * valueDestiny
                val conversionString =
                    NumberFormat.getCurrencyInstance(Locale.US).format(conversion)
                convertedValue.postValue(conversionString)
            } else {
                convertedValue.postValue("$0.0000")
            }
        }
    }


}