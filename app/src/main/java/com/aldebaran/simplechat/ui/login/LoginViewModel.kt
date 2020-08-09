package com.aldebaran.simplechat.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aldebaran.simplechat.data.SharedPref
import com.aldebaran.simplechat.helper.Result
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(
    private var pref: SharedPref
): ViewModel() {

    val validator by lazy { MutableLiveData<Int>() }
    val loginState by lazy { MutableLiveData<Result>() }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    fun getLoginStatus() = pref.getIsLogin()

    fun validator(email: String, password: String){
        if(email.isEmpty() || !email.contains("@")) {
            validator.value = 0
        }
        else if(password.isEmpty() || password.length < 6) {
            validator.value = 1
        }
        else validator.value = 2
    }

    fun doLogin(email: String, password: String){
        loginState.value = Result.LOADING
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            loginState.value = if(task.isSuccessful){
                Result.SUCCESS
            } else Result.ERROR
        }
    }

    fun saveSession(email: String){
        val split = email.split("@")
        pref.saveUserName(split.first())
        pref.saveIsLogin(true)
    }
}