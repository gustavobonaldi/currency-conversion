package br.com.bonaldi.currency.conversion.presentation.currencylist

import androidx.lifecycle.*
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.dto.currency.Currencies
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCaseImpl
import br.com.bonaldi.currency.conversion.presentation.BaseViewModel

class CurrencyListViewModel(
    private val currencyLayerUseCase: CurrencyLayerUseCaseImpl
    ) : BaseViewModel(), Conversions {

    private val _getCurrencies = MutableLiveData<Resource<Any>>()
    val getCurrencies: LiveData<Resource<Any>> = _getCurrencies

    override fun updateCurrencies() {
        launch {
            currencyLayerUseCase.getCurrencies()
        }
    }

    override fun addCurrenciesObserver(
        lifecycleOwner: LifecycleOwner,
        onSuccess: (Currencies) -> Unit
    ){
        currencyLayerUseCase.getCurrenciesLiveData()?.observe(lifecycleOwner, Observer {currencies ->
            onSuccess(currencies)
        })
    }
}