package br.com.bonaldi.currency.conversion.presentation.conversions

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCaseImpl
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesDTO
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.presentation.BaseViewModel
import br.com.bonaldi.currency.conversion.presentation.currencylist.Conversions
import java.text.NumberFormat
import java.util.*

class ConversionViewModel(private val currencyLayerUseCase: CurrencyLayerUseCase) : BaseViewModel(), Conversions.List{

    private val _realTimeRates = MutableLiveData<QuotesDTO>()
    private val realTimeRatesLiveData: LiveData<QuotesDTO> = _realTimeRates

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

    override fun getConversionFromTo(currencyFrom: Pair<String, String>?, currencyTo: Pair<String, String>?, valueToConvert: Double, onSuccess: (String) -> Unit, onError: (ErrorDTO) -> Unit?)
    {
        if(currencyFrom == null || currencyTo == null || currencyFrom.first.isEmpty() || currencyTo.first.isEmpty()){
            onError.invoke(ErrorDTO(errorMessage =  R.string.currencies_not_selected))
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
                onSuccess.invoke(conversionString)
            } else {
                onSuccess.invoke("$0.0000")
            }
        }
    }
}