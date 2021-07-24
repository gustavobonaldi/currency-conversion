package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO

@Dao
interface CurrencyRateDao: BaseDao<RatesDTO> {
    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getAll(): List<RatesDTO>

    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getRatesLiveData(): LiveData<List<RatesDTO>?>
}