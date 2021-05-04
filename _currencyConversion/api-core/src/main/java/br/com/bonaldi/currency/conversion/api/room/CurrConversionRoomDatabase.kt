package br.com.bonaldi.currency.conversion.api.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesDTO
import br.com.bonaldi.currency.conversion.api.room.dao.CurrencyDao

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [CurrenciesDTO::class, QuotesDTO::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class CurrConversionRoomDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    companion object {
        val db_name = "currency_conversion_database"
    }
}