package com.aldebaran.simplechat

import android.app.Application
import com.aldebaran.simplechat.di.AppComponent
import com.aldebaran.simplechat.di.AppModule
import com.aldebaran.simplechat.di.DaggerAppComponent
import com.aldebaran.simplechat.di.RoomModule

class MyApp: Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .roomModule(RoomModule())
            .build()
    }

    fun getComponent() = appComponent
}