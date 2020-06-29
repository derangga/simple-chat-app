package com.aldebaran.simplechat.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg data: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(vararg data: T)

    @Delete
    suspend fun delete(data: T)
}