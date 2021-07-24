package br.com.bonaldi.currency.conversion.api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_currency")
data class CurrencyDTO(
    @PrimaryKey
    val currencyCode: String = "",
    val currencyCountry: String = "",
    var type: CurrencyType = CurrencyType.FROM
): Serializable{



    enum class CurrencyType {
        FROM,
        TO
    }
}




