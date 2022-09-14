package br.com.bonaldi.currency.conversion.core.database.di

import android.content.Context
import br.com.bonaldi.currency.conversion.core.database.cache.CurrencyDataStore
import br.com.bonaldi.currency.conversion.core.database.cache.CurrencyDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @Provides
    @Singleton
    fun provideCurrencyDataStore(
        @ApplicationContext context: Context,
    ): CurrencyDataStore = CurrencyDataStoreImpl(context)
}
