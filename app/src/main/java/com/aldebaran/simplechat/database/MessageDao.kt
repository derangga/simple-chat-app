package com.aldebaran.simplechat.database

import androidx.room.Dao
import androidx.room.Query
import com.aldebaran.simplechat.database.table.Messages
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao: BaseDao<Messages> {

    @Query("select * from tbl_message order by createdAt desc")
    fun getAllChat(): Flow<List<Messages>>

    @Query("delete from tbl_message")
    suspend fun truncateMessage()
}