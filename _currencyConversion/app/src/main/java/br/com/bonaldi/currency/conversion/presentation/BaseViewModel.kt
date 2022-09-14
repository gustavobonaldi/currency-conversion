package br.com.bonaldi.currency.conversion.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
    fun launch(launch: suspend () -> Unit) {
        viewModelScope.launch {
            launch.invoke()
        }
    }
}