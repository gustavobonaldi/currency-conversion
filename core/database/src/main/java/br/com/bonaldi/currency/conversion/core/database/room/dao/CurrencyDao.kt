package br.com.bonaldi.currency.conversion.core.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao: BaseDao<Currency> {
    @Query("SELECT * from tb_currency ORDER BY isFavorite DESC, isRecentUse DESC, updateMillis DESC")
    suspend fun getAll(): List<Currency>

    @Query("SELECT * from tb_currency ORDER BY isFavorite DESC, isRecentUse DESC, updateMillis DESC")
    fun getCurrenciesFlow(): Flow<List<Currency>>

    @Query("DELETE FROM tb_currency")
    suspend fun deleteAll()

    @Query("UPDATE tb_currency SET isRecentUse = :recentlyUsed , updateMillis = :timeMillis  WHERE code = :currencyCode")
    suspend fun updateRecentlyUsedCurrency(currencyCode: String, recentlyUsed: Boolean, timeMillis: Long)

    @Query("UPDATE tb_currency SET isFavorite = :isFavorite WHERE code = :currencyCode")
    suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean)

    @Query("SELECT * from tb_currency WHERE isRecentUse = 1 AND isFavorite = 0 ORDER BY updateMillis DESC")
    suspend fun selectRecentlyUsedCurrencies(): List<Currency>

    @Transaction
    suspend fun setCurrencyList(list: List<Currency>){
        val localCurrenciesList = getAll()
        if(!localCurrenciesList.isNullOrEmpty()) {
            val localCurrencies = getAll().associateBy {
                it.code
            }

            for (currency in list) {
                if (!localCurrencies.containsKey(currency.code)) {
                    insert(currency)
                } else {
                    localCurrencies.get(currency.code)?.country =
                        currency.country
                }
            }
        }
        else {
            insertAll(list)
        }
    }
}