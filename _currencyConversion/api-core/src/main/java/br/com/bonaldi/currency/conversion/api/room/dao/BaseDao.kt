package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(list: List<T>)

     @Delete
     fun delete(item: T)
}