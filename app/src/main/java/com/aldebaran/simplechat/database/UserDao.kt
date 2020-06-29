package com.aldebaran.simplechat.database

import androidx.room.Dao
import androidx.room.Query
import com.aldebaran.simplechat.database.table.User

@Dao
interface UserDao: BaseDao<User> {

    @Query("delete from tbl_user")
    suspend fun truncateUser()

    @Query("select * from tbl_user where name = :userName")
    suspend fun getUser(userName: String): User?
}