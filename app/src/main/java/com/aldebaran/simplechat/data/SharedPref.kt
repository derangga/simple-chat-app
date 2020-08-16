package com.aldebaran.simplechat.data

import android.content.Context

class SharedPref(context: Context) {

    private val sharedPrefs by lazy { context.getSharedPreferences(KEY, Context.MODE_PRIVATE) }
    private val IS_LOGIN = "IS_LOGIN"
    private val USER = "userName"
    private val KEY = "session"

    fun saveIsLogin(status: Boolean){
        sharedPrefs.edit()
        .putBoolean(IS_LOGIN, status)
        .apply()
    }

    fun saveUserName(userName: String){
        sharedPrefs.edit()
            .putString(USER, userName)
            .apply()
    }

    fun getIsLogin(): Boolean = sharedPrefs.getBoolean(IS_LOGIN, false)
    fun getUserName(): String = sharedPrefs.getString(USER, "").orEmpty()
    fun clearPref(){
        sharedPrefs.edit().clear().apply()
    }
}