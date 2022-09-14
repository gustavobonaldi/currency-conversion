package br.com.bonaldi.currency.conversion.di

import br.com.bonaldi.currency.conversion.api.BuildConfig
import br.com.bonaldi.currency.conversion.api.api.config.NetworkUtils
import br.com.bonaldi.currency.conversion.data.api.CurrencyLayerServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): CurrencyLayerServices = NetworkUtils.provideRetrofit(
        webServiceApi = CurrencyLayerServices::class.java,
        apiUrl = BuildConfig.CURRENCY_LAYER_URL,
        okHttpClient = httpClient
    )
}