package br.com.bonaldi.currency.conversion.core.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import br.com.bonaldi.currency.conversion.core.database.model.history.ConversionHistory
import br.com.bonaldi.currency.conversion.core.database.room.dao.ConversionHistoryDao
import br.com.bonaldi.currency.conversion.core.database.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.core.database.room.dao.CurrencyRateDao

@Database(
    entities = [
        Currency::class,
        Rates::class,
        ConversionHistory::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CurrConversionRoomDatabase : RoomDatabase() {
    abstract fun currenciesDao(): CurrencyDao
    abstract fun ratesDao(): CurrencyRateDao
    abstract fun historyDao(): ConversionHistoryDao

    companion object {
        const val db_name = "currency_conversion_database"
    }
}