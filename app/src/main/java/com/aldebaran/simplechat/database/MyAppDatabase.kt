package com.aldebaran.simplechat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aldebaran.simplechat.database.table.Messages
import com.aldebaran.simplechat.database.table.User

@Database(entities = [Messages::class, User::class], version = 2, exportSchema = false)
abstract class MyAppDatabase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao
}