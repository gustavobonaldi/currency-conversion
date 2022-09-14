package br.com.bonaldi.currency.conversion.core.database.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_currency")
data class CurrencyModel(
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

    fun getFormattedString(): String{
        return ("$currencyCode - $currencyCountry")
    }
}




