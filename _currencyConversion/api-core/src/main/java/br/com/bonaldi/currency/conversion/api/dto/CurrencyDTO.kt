package br.com.bonaldi.currency.conversion.api.dto

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_currency")
data class CurrencyDTO(
    @PrimaryKey
    val currencyCode: String,
    var currencyCountry: String?,
    val recentlyUsed: Boolean = false,
    val isFavorite: Boolean = false,
    val updateTimeMillis: Long = 0L
): Serializable{
    @Ignore var selectionType: CurrencyType? = null

    enum class CurrencyType {
        FROM,
        TO
    }
}




