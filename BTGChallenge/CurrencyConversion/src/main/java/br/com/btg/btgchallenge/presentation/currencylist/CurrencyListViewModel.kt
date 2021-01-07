package br.com.btg.btgchallenge.presentation.currencylist

import androidx.lifecycle.*
import br.com.btg.btgchallenge.network.api.config.Resource
import br.com.btg.btgchallenge.network.api.config.Status
import br.com.btg.btgchallenge.data.repository.CurrencyLayerRepositoryImpl
import br.com.btg.btgchallenge.domain.CurrencyLayerUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyListViewModel(val currencyLayerUseCase: CurrencyLayerUseCaseImpl) : ViewModel() {

    val getCurrencies= MutableLiveData<Resource<Any>>()

    fun getCurrencies() {
        viewModelScope.launch(context = Dispatchers.IO)
        {
            getCurrencies.postValue(Resource.loading())
            val currencies = currencyLayerUseCase.getCurrencies()
            when (currencies.status) {
                Status.SUCCESS -> {
                    //persistir dado
                }
            }
            getCurrencies.postValue(currencies)
        }
    }
}