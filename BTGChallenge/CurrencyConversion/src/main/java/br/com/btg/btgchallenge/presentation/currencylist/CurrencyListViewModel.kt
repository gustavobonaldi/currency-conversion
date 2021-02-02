package br.com.btg.btgchallenge.presentation.currencylist

import androidx.lifecycle.*
import br.com.btg.btgchallenge.api.api.config.Resource
import br.com.btg.btgchallenge.domain.CurrencyLayerUseCaseImpl
import br.com.btg.btgchallenge.presentation.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyListViewModel(
    private val currencyLayerUseCase: CurrencyLayerUseCaseImpl
    ) : BaseViewModel(), Conversions {

    private val _getCurrencies = MutableLiveData<Resource<Any>>()
    val getCurrencies: LiveData<Resource<Any>> = _getCurrencies

    override fun getCurrencies() {
        launch {
            _getCurrencies.postValue(Resource.loading())
            _getCurrencies.postValue(currencyLayerUseCase.getCurrencies())
        }
    }
}