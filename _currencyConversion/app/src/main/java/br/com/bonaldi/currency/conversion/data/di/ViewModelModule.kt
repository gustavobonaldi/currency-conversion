package br.com.bonaldi.currency.conversion.data.di

import br.com.bonaldi.currency.conversion.presentation.conversions.ConversionViewModel
import br.com.bonaldi.currency.conversion.presentation.currencylist.CurrencyListViewModel
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
