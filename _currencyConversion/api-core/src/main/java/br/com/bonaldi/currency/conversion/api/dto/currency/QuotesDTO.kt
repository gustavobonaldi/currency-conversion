package br.com.bonaldi.currency.conversion.api.dto.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.bonaldi.currency.conversion.api.dto.ApiResponse

@Entity(tableName = "quotes_table")
data class QuotesDTO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")var id : Int = 0,
    @ColumnInfo(name = "quotes")var quotes: HashMap<String, Double>?,
    @ColumnInfo(name = "lastUpdate") var lastUpdate: Int?
)