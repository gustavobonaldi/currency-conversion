package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO

@Dao
interface CurrencyDao: BaseDao<CurrencyDTO> {
    @Query("SELECT * from tb_currency ORDER BY currencyCode DESC LIMIT 1")
    fun getAll(): List<CurrencyDTO>

    @Query("SELECT * from tb_currency ORDER BY currencyCode DESC LIMIT 1")
    fun getCurrenciesLiveData(): LiveData<List<CurrencyDTO>?>

    @Query("DELETE FROM tb_currency")
    fun deleteAll()

    @Transaction
    fun setCurrencyList(list: List<CurrencyDTO>){
        deleteAll()
        setCurrencyList(list)
    }

//    @Query("UPDATE currencies_table SET isRecent = :isRecent WHERE  = :currencyId")
//    suspend fun updateCurrencyRecentValue(isRecent: Boolean, currencyId: String): CurrenciesDTO?
}