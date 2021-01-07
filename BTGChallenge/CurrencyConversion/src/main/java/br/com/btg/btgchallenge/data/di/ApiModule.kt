package br.com.btg.btgchallenge.data.di

import br.com.btg.btgchallenge.api.BuildConfig
import br.com.btg.btgchallenge.api.api.config.NetworkUtils
import br.com.btg.btgchallenge.api.api.config.RequestInterceptor
import br.com.btg.btgchallenge.api.api.config.ResponseHandler
import br.com.btg.btgchallenge.data.repository.CurrencyLayerRepositoryImpl
import br.com.btg.btgchallenge.data.api.CurrencyLayerServices
import br.com.btg.btgchallenge.data.repository.CurrencyRepositoryLocal
import br.com.btg.btgchallenge.domain.CurrencyLayerUseCaseImpl
import org.koin.dsl.module

val apiModule = module {
    factory { RequestInterceptor() }
    factory { NetworkUtils.provideOkHttpClient(get()) }
    factory { NetworkUtils.provideRetrofit(webServiceApi = CurrencyLayerServices::class.java, apiUrl = BuildConfig.CURRENCY_LAYER_URL, okHttpClient = get()) }
    factory { ResponseHandler() }


    single  { CurrencyLayerRepositoryImpl(get(), get(), get(), get()) }
    single  { CurrencyLayerUseCaseImpl(get())}
    single  { CurrencyRepositoryLocal(get()) }
}