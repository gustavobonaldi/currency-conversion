package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.bonaldi.currency.conversion.api.model.RatesModel

@Dao
interface CurrencyRateDao: BaseDao<RatesModel> {
    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getAll(): List<RatesModel>

    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getRatesLiveData(): LiveData<List<RatesModel>?>
}