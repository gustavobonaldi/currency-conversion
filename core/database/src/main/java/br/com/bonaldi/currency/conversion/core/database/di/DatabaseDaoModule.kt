package br.com.bonaldi.currency.conversion.core.database.di

import br.com.bonaldi.currency.conversion.core.database.room.CurrConversionRoomDatabase
import br.com.bonaldi.currency.conversion.core.database.room.dao.ConversionHistoryDao
import br.com.bonaldi.currency.conversion.core.database.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.core.database.room.dao.CurrencyRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseDaoModule {
    @Provides
    @Singleton
    fun providesConversionDao(
        database: CurrConversionRoomDatabase,
    ): CurrencyDao = database.currenciesDao()

    @Provides
    @Singleton
    fun provideRatesDao(
        database: CurrConversionRoomDatabase,
    ): CurrencyRateDao = database.ratesDao()

    @Provides
    @Singleton
    fun provideHistoryDao(
        database: CurrConversionRoomDatabase,
    ): ConversionHistoryDao = database.historyDao()
}
