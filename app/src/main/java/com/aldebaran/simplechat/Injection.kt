package com.aldebaran.simplechat

import android.content.Context
import com.aldebaran.simplechat.data.SharedPref
import com.aldebaran.simplechat.ui.login.LoginFactory
import com.aldebaran.simplechat.ui.room.MainFactory
import com.aldebaran.simplechat.ui.room.MainViewModel
import com.aldebaran.simplechat.ui.room.MessageAdapter

object Injection {

    fun provideSharedPref(context: Context): SharedPref{
        return SharedPref(context)
    }

    fun provideLoginFactory(context: Context): LoginFactory{
        val sharedPref = provideSharedPref(context)
        return LoginFactory(sharedPref)
    }

    fun provideMainFactory(context: Context): MainFactory{
        val sharedPref = provideSharedPref(context)
        return MainFactory(sharedPref)
    }

    fun provideMessageAdapter(viewModel: MainViewModel, userName: String): MessageAdapter{
        return MessageAdapter(viewModel, userName)
    }
}