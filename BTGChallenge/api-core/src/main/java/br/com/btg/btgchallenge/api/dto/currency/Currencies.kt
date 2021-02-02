package br.com.btg.btgchallenge.api.dto.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies_table")
data class Currencies(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")var id : Int = 0,
    @ColumnInfo(name = "currencies") val currencies: HashMap<String, String>,
    @ColumnInfo(name = "lastUpdate") var lastUpdate: Int?
) {
}