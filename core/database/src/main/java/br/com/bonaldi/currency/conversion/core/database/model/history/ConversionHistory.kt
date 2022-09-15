package br.com.bonaldi.currency.conversion.core.database.model.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_conversion_history")
data class ConversionHistory(
    @PrimaryKey val id: String,
    val originCurrencyCode: String,
    val destinyCurrencyCode: String
) {
    companion object {
        fun generateId(originCurrencyCode: String, destinyCurrencyCode: String): String {
            return "$originCurrencyCode$destinyCurrencyCode"
        }
    }
}