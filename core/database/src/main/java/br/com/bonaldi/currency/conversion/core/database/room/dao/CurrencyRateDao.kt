package br.com.bonaldi.currency.conversion.core.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao: BaseDao<Rates> {
    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    suspend fun getAll(): List<Rates>

    @Query("SELECT * from tb_rates ORDER BY currencyCode DESC")
    fun getRatesFlow(): Flow<List<Rates>>
}