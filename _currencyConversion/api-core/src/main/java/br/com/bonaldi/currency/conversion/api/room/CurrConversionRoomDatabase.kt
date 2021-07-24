package br.com.bonaldi.currency.conversion.api.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyRateDao

@Database(
    entities = [
        CurrencyDTO::class,
        RatesDTO::class
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