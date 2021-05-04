package br.com.bonaldi.currency.conversion.api.room.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.bonaldi.currency.conversion.api.dto.currency.CurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.QuotesDTO

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: CurrenciesDTO)

    @Query("DELETE FROM currencies_table")
    suspend fun deleteCurrencies()

    @Query("SELECT * from currencies_table ORDER BY id DESC LIMIT 1")
    suspend fun getCurrencies(): CurrenciesDTO?

    @Query("SELECT * from currencies_table ORDER BY id DESC LIMIT 1")
    fun getCurrenciesLiveData(): LiveData<CurrenciesDTO>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: QuotesDTO)

    @Query("DELETE FROM quotes_table")
    suspend fun deleteQuotes()

    @Query("SELECT * from quotes_table ORDER BY id DESC LIMIT 1")
    suspend fun getQuotes(): QuotesDTO

    @Query("SELECT * from quotes_table ORDER BY id DESC LIMIT 1")
    fun getQuotesLiveData(): LiveData<QuotesDTO>?
}