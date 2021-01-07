package br.com.btg.btgchallenge.presentation.currencylist

import androidx.lifecycle.*
import br.com.btg.btgchallenge.api.api.config.Resource
import br.com.btg.btgchallenge.domain.CurrencyLayerUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyListViewModel(val currencyLayerUseCase: CurrencyLayerUseCaseImpl) : ViewModel() {

    private val _getCurrencies = MutableLiveData<Resource<Any>>()
    val getCurrencies: LiveData<Resource<Any>> = _getCurrencies

    fun getCurrencies() {
        viewModelScope.launch(context = Dispatchers.IO)
        {
            _getCurrencies.postValue(Resource.loading())
            _getCurrencies.postValue(currencyLayerUseCase.getCurrencies())
        }
    }
}