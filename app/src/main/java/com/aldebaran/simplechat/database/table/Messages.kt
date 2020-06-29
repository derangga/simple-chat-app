package com.aldebaran.simplechat.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_message")
data class Messages(
    @PrimaryKey
    @ColumnInfo(name = "chatId")
    val timestamp: Long = 0,

    @ColumnInfo(name = "from")
    val from: String? = "",

    @ColumnInfo(name = "message")
    val message: String? = "",

    @ColumnInfo(name = "createdAt")
    var createdAt: String? = ""
)