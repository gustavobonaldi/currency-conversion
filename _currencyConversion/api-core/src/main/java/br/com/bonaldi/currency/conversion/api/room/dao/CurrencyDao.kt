package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO2

@Dao
interface CurrencyDao: BaseDao<CurrencyDTO2> {
    @Query("SELECT * from tb_currency ORDER BY currencyCode DESC LIMIT 1")
    fun getAll(): List<CurrencyDTO2>

    @Query("SELECT * from tb_currency ORDER BY currencyCode DESC LIMIT 1")
    fun getCurrenciesLiveData(): LiveData<List<CurrencyDTO2>?>

//    @Query("UPDATE currencies_table SET isRecent = :isRecent WHERE  = :currencyId")
//    suspend fun updateCurrencyRecentValue(isRecent: Boolean, currencyId: String): CurrenciesDTO?
}