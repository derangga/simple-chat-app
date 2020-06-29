package com.aldebaran.simplechat.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val userId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String? = "",

    @ColumnInfo(name = "email")
    val email: String? = "",

    @ColumnInfo(name = "avatar")
    val avatar: String? = ""
)