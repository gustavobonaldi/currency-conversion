package br.com.bonaldi.currency.conversion.data.di

import androidx.room.Room
import br.com.bonaldi.currency.conversion.api.BuildConfig
import br.com.bonaldi.currency.conversion.api.api.config.NetworkUtils
import br.com.bonaldi.currency.conversion.api.api.config.RequestInterceptor
import br.com.bonaldi.currency.conversion.api.api.config.ErrorHandler
import br.com.bonaldi.currency.conversion.api.cache.CurrencyDataStore
import br.com.bonaldi.currency.conversion.api.cache.CurrencyDataStoreImpl
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
    factory { ErrorHandler() }
    factory <CurrencyDataStore>{CurrencyDataStoreImpl(get())}


    single <CurrencyLayerRepository> { CurrencyLayerRepositoryImpl(get(), get(), get(), get()) }
    single <CurrencyLayerUseCase> { CurrencyLayerUseCaseImpl(get()) }
    single { Room.databaseBuilder(get(), CurrConversionRoomDatabase::class.java, CurrConversionRoomDatabase.db_name).build()}
    single { get<CurrConversionRoomDatabase>().currenciesDao() }
    single { get<CurrConversionRoomDatabase>().ratesDao() }

    viewModel { ConversionViewModel(get(), get()) }
}