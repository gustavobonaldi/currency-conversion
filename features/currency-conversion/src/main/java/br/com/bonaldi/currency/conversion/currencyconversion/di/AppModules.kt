package br.com.bonaldi.currency.conversion.currencyconversion.di

import br.com.bonaldi.currency.conversion.currencyconversion.data.repository.CurrencyLayerRepository
import br.com.bonaldi.currency.conversion.currencyconversion.data.repository.CurrencyLayerRepositoryImpl
import br.com.bonaldi.currency.conversion.currencyconversion.domain.CurrencyLayerUseCase
import br.com.bonaldi.currency.conversion.currencyconversion.domain.CurrencyLayerUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {
    @Binds
    fun bindsCurrencyRepository(
        currencyRepository: CurrencyLayerRepositoryImpl
    ): CurrencyLayerRepository

    @Binds
    fun bindsCurrencyUseCase(
        authorsRepository: CurrencyLayerUseCaseImpl
    ): CurrencyLayerUseCase
}
