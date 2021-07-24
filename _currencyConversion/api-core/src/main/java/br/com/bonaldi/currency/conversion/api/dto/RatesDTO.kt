package br.com.bonaldi.currency.conversion.api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tb_rates")
data class RatesDTO(
    @PrimaryKey
    val currencyCode: String,
    val currencyValueInDollar: Double
): Serializable