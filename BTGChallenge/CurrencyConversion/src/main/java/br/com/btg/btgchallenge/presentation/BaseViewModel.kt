package br.com.btg.btgchallenge.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {

    fun launch(launch: suspend () -> Unit)
    {
        viewModelScope.launch(context = Dispatchers.IO){
            launch.invoke()
        }
    }
}