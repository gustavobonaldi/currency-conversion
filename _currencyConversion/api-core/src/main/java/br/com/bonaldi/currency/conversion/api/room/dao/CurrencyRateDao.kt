package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao: BaseDao<RatesModel> {
    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getAll(): List<RatesModel>

    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getRatesFlow(): Flow<List<RatesModel>>
}