package br.com.bonaldi.currency.conversion.api.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     @JvmSuppressWildcards
     suspend fun insertAll(list: List<T>)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(item: T)

     @Delete
     @JvmSuppressWildcards
     suspend fun delete(item: T)
}