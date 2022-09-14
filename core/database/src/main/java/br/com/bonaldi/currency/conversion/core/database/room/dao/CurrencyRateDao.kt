package br.com.bonaldi.currency.conversion.core.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.bonaldi.currency.conversion.core.database.model.RatesModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao: BaseDao<RatesModel> {
    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    suspend fun getAll(): List<RatesModel>

    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getRatesFlow(): Flow<List<RatesModel>>
}