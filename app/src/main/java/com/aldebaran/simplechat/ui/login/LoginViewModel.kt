package com.aldebaran.simplechat.ui.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aldebaran.simplechat.data.SharedPref
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(
    private var pref: SharedPref
): ViewModel() {

    val validator by lazy { MutableLiveData<Credential>() }
    val loginState by lazy { MutableLiveData<Result>() }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    fun getLoginStatus() = pref.getIsLogin()

    fun checkCredential(email: String, password: String){
        validator.value = when {
            isInvalidEmail(email) -> {
                Credential.INVALID_EMAIL
            }
            isInvalidPassword(password) -> {
                Credential.INVALID_PASSWORD
            }
            else -> Credential.ALL_DATA_VALID
        }
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

    private fun isInvalidEmail(email: String): Boolean{
        return email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isInvalidPassword(password: String): Boolean{
        return password.isEmpty() || password.length < 6
    }

    fun saveSession(email: String){
        val split = email.split("@")
        pref.saveUserName(split.first())
        pref.saveIsLogin(true)
    }
}