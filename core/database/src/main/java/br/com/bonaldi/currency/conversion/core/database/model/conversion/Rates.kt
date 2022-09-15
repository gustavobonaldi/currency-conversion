package br.com.bonaldi.currency.conversion.core.database.model.conversion

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_rates")
data class Rates(
    @PrimaryKey
    val currencyCode: String,
    val currencyValueInDollar: Double
): Serializable