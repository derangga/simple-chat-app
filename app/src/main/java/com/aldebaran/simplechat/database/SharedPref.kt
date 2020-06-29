package com.aldebaran.simplechat.database

import android.content.Context
import javax.inject.Inject

class SharedPref @Inject constructor(
    context: Context
) {

    private val IS_LOGIN = "is_login"
    private val USER = "user_name"
    private val KEY = "USER_PREFS"
    private var sharedPrefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)

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