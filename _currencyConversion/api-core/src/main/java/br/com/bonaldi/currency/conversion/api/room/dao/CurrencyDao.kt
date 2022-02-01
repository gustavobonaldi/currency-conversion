package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao: BaseDao<CurrencyModel> {
    @Query("SELECT * from tb_currency ORDER BY isFavorite DESC, recentlyUsed DESC, updateTimeMillis DESC")
    suspend fun getAll(): List<CurrencyModel>

    @Query("SELECT * from tb_currency ORDER BY isFavorite DESC, recentlyUsed DESC, updateTimeMillis DESC")
    fun getCurrenciesFlow(): Flow<List<CurrencyModel>>

    @Query("DELETE FROM tb_currency")
    suspend fun deleteAll()

    @Query("UPDATE tb_currency SET recentlyUsed = :recentlyUsed , updateTimeMillis = :timeMillis  WHERE currencyCode = :currencyCode")
    suspend fun updateRecentlyUsedCurrency(currencyCode: String, recentlyUsed: Boolean, timeMillis: Long)

    @Query("UPDATE tb_currency SET isFavorite = :isFavorite WHERE currencyCode = :currencyCode")
    suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean)

    @Query("SELECT * from tb_currency WHERE recentlyUsed = 1 AND isFavorite = 0 ORDER BY updateTimeMillis DESC")
    suspend fun selectRecentlyUsedCurrencies(): List<CurrencyModel>

    @Transaction
    suspend fun setCurrencyList(list: List<CurrencyModel>){
        val localCurrenciesList = getAll()
        if(!localCurrenciesList.isNullOrEmpty()) {
            val localCurrencies = getAll().associateBy {
                it.currencyCode
            }

            for (currency in list) {
                if (!localCurrencies.containsKey(currency.currencyCode)) {
                    insert(currency)
                } else {
                    localCurrencies.get(currency.currencyCode)?.currencyCountry =
                        currency.currencyCountry
                }
            }
        }
        else {
            insertAll(list)
        }
    }
}