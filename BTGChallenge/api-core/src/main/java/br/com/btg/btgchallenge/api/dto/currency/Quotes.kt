package br.com.btg.btgchallenge.api.dto.currency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes_table")
data class Quotes(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")var id : Int = 0,
    @ColumnInfo(name = "quotes")var quotes: HashMap<String, Double>,
    @ColumnInfo(name = "lastUpdate") var lastUpdate: Int?
) {

}