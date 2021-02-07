package br.com.bonaldi.currency.conversion.data.di

import androidx.room.Room
import br.com.bonaldi.currency.conversion.api.BuildConfig
import br.com.bonaldi.currency.conversion.api.api.config.NetworkUtils
import br.com.bonaldi.currency.conversion.api.api.config.RequestInterceptor
import br.com.bonaldi.currency.conversion.api.api.config.ResponseHandler
import br.com.bonaldi.currency.conversion.api.room.CurrConversionRoomDatabase
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepositoryImpl
import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import br.com.bonaldi.currency.conversion.domain.CurrencyLayerUseCaseImpl
import org.koin.dsl.module

val apiModule = module {
    factory { RequestInterceptor() }
    factory { NetworkUtils.provideOkHttpClient(get()) }
    factory { NetworkUtils.provideRetrofit(webServiceApi = CurrencyLayerServices::class.java, apiUrl = BuildConfig.CURRENCY_LAYER_URL, okHttpClient = get()) }
    factory { ResponseHandler() }


    single  { CurrencyLayerRepositoryImpl(get(), get(), get(), get()) }
    single  { CurrencyLayerUseCaseImpl(get()) }
    single  { Room.databaseBuilder(get(), CurrConversionRoomDatabase::class.java, CurrConversionRoomDatabase.db_name).build()}
    single  { get<CurrConversionRoomDatabase>().currencyDao() }
}