package com.aldebaran.simplechat.di

import android.content.Context
import androidx.room.Room
import com.aldebaran.simplechat.database.MessageDao
import com.aldebaran.simplechat.database.MyAppDatabase
import com.aldebaran.simplechat.database.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): MyAppDatabase {
        return Room.databaseBuilder(context, MyAppDatabase::class.java, "SimpleChatDb")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(myDatabase: MyAppDatabase): UserDao = myDatabase.userDao()

    @Provides
    @Singleton
    fun provideReceivedMessagesDao(myDatabase: MyAppDatabase): MessageDao
        = myDatabase.messageDao()

}