package br.com.bonaldi.currency.conversion.core.database.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.history.ConversionHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversionHistoryDao: BaseDao<ConversionHistory> {
    @Query("SELECT * from tb_currency JOIN tb_conversion_history ON code = originCurrencyCode OR code = destinyCurrencyCode")
    suspend fun getAll(): Map<ConversionHistory, List<Currency>>

    @Query("SELECT * from tb_currency JOIN tb_conversion_history ON code = originCurrencyCode OR code = destinyCurrencyCode")
    fun getConversionHistory(): Flow<Map<ConversionHistory, List<Currency>>>
}