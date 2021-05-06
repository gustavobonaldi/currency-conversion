package br.com.bonaldi.currency.conversion.data.di

import androidx.room.Room
import br.com.bonaldi.currency.conversion.api.BuildConfig
import br.com.bonaldi.currency.conversion.api.api.config.NetworkUtils
import br.com.bonaldi.currency.conversion.api.api.config.RequestInterceptor
import br.com.bonaldi.currency.conversion.api.api.config.ResponseHandler
import br.com.bonaldi.currency.conversion.api.room.CurrConversionRoomDatabase
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepositoryImpl
import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepository
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCaseImpl
import br.com.bonaldi.currency.conversion.presentation.ConversionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { RequestInterceptor() }
    factory { NetworkUtils.provideOkHttpClient(get()) }
    factory { NetworkUtils.provideRetrofit(webServiceApi = CurrencyLayerServices::class.java, apiUrl = BuildConfig.CURRENCY_LAYER_URL, okHttpClient = get()) }
    factory { ResponseHandler() }


    single<CurrencyLayerRepository> { CurrencyLayerRepositoryImpl(get(), get(), get()) }
    single<CurrencyLayerUseCase> { CurrencyLayerUseCaseImpl(get()) }
    single { Room.databaseBuilder(get(), CurrConversionRoomDatabase::class.java, CurrConversionRoomDatabase.db_name).build()}
    single { get<CurrConversionRoomDatabase>().currencyDao() }

    viewModel { ConversionViewModel(get()) }
}