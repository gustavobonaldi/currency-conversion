package br.com.btg.btgchallenge.data.di

import br.com.btg.btgchallenge.presentation.conversions.ConversionViewModel
import br.com.btg.btgchallenge.presentation.currencylist.CurrencyListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        CurrencyListViewModel(get())
    }
    viewModel {
        ConversionViewModel(get())
    }
}
