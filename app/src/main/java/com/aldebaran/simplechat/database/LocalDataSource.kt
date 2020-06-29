package com.aldebaran.simplechat.database

import androidx.lifecycle.asLiveData
import com.aldebaran.simplechat.database.table.Messages
import com.aldebaran.simplechat.database.table.User
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val userDao: UserDao,
    private val messageDao: MessageDao
) {

    fun getAllMessage() = messageDao.getAllChat().asLiveData()

    suspend fun insertMessage(messages: List<Messages>){
        messageDao.insertAll(*messages.toTypedArray())
    }

    suspend fun insertUser(user: User){
        userDao.insert(user)
    }

    suspend fun getUser(userName: String): User? {
        return userDao.getUser(userName)
    }

    suspend fun truncateUser(){
        userDao.truncateUser()
    }

    suspend fun truncateMessage(){
        messageDao.truncateMessage()
    }
}