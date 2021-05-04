package br.com.bonaldi.currency.conversion.api.dto.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.bonaldi.currency.conversion.api.dto.ApiResponse

@Entity(tableName = "currencies_table")
data class CurrenciesDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")var id : Int = 0,
    @ColumnInfo(name = "currencies") val currencies: HashMap<String, String>?,
    @ColumnInfo(name = "lastUpdate") var lastUpdate: Int?
)