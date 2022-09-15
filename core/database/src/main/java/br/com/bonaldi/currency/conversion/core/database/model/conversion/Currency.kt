package br.com.bonaldi.currency.conversion.core.database.model.conversion

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_currency")
data class Currency(
    @PrimaryKey val code: String,
    var country: String?,
    val isRecentUse: Boolean = false,
    val isFavorite: Boolean = false,
    val updateMillis: Long = 0L
) : Serializable {
    @Ignore
    var selectionType: CurrencyType? = null

    enum class CurrencyType {
        FROM,
        TO
    }

    override fun toString(): String {
        return ("$code - $country")
    }
}




