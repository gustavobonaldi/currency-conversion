package br.com.bonaldi.currency.conversion.api.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyRateDao

@Database(
    entities = [
        CurrencyModel::class,
        RatesModel::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CurrConversionRoomDatabase: RoomDatabase() {
    abstract fun currenciesDao(): CurrencyDao
    abstract fun ratesDao(): CurrencyRateDao

    companion object {
        const val db_name = "currency_conversion_database"
    }
}