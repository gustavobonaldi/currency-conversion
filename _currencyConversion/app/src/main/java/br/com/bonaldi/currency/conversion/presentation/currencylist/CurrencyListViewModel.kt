package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.*
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.presentation.BaseViewModel

class CurrencyListViewModel(private val currencyLayerUseCase: CurrencyLayerUseCase) : BaseViewModel(), Conversions {

    private val _getCurrencies = MutableLiveData<Resource<Any>>()
    val getCurrencies: LiveData<Resource<Any>> = _getCurrencies

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