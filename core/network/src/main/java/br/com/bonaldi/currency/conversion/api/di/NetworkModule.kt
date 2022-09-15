package br.com.bonaldi.currency.conversion.api.di

import br.com.bonaldi.currency.conversion.api.api.config.ErrorHandler
import br.com.bonaldi.currency.conversion.api.api.config.NetworkUtils
import br.com.bonaldi.currency.conversion.api.api.config.RequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRequestInterceptor(): RequestInterceptor = RequestInterceptor()

    @Provides
    fun provideHttpClient(requestInterceptor: RequestInterceptor): OkHttpClient =
        NetworkUtils.provideOkHttpClient(requestInterceptor)

    @Provides
    fun provideErrorHandler() = ErrorHandler()
}