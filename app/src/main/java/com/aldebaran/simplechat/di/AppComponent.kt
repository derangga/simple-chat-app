package com.aldebaran.simplechat.di

import com.aldebaran.simplechat.ui.login.LoginActivity
import com.aldebaran.simplechat.ui.room.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RoomModule::class])
@Singleton
interface AppComponent {
    fun inject(loginActivity: LoginActivity)
    fun inject(mainActivity: MainActivity)
}