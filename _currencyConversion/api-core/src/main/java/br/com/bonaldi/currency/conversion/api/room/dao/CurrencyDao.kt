package br.com.bonaldi.currency.conversion.api.room.dao
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.bonaldi.currency.conversion.api.dto.currency.Currencies
import br.com.bonaldi.currency.conversion.api.dto.currency.Quotes
import br.com.bonaldi.currency.conversion.api.room.CurrConversionRoomDatabase

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: Currencies)

    @Query("DELETE FROM currencies_table")
    suspend fun deleteCurrencies()

    @Query("SELECT * from currencies_table ORDER BY id DESC LIMIT 1")
    suspend fun getCurrencies(): Currencies?

    @Query("SELECT * from currencies_table ORDER BY id DESC LIMIT 1")
    fun getCurrenciesLiveData(): LiveData<Currencies>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: Quotes)

    @Query("DELETE FROM quotes_table")
    suspend fun deleteQuotes()

    @Query("SELECT * from quotes_table ORDER BY id DESC LIMIT 1")
    suspend fun getQuotes(): Quotes

    @Query("SELECT * from quotes_table ORDER BY id DESC LIMIT 1")
    fun getQuotesLiveData(): LiveData<Quotes>?
}