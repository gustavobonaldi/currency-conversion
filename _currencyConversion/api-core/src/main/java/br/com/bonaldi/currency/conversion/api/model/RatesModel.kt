package br.com.bonaldi.currency.conversion.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_rates")
data class RatesModel(
    @PrimaryKey
    val currencyCode: String,
    val currencyValueInDollar: Double
): Serializable