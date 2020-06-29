package com.aldebaran.simplechat.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldebaran.simplechat.database.LocalDataSource
import com.aldebaran.simplechat.database.SharedPref
import com.aldebaran.simplechat.database.table.User
import com.aldebaran.simplechat.helper.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class LoginViewModel(
    private val localSource: LocalDataSource,
    private var pref: SharedPref
): ViewModel() {

    val validator by lazy { MutableLiveData<Int>() }
    val loginState by lazy { MutableLiveData<Result>() }
    private val firebaseDb by lazy {
        FirebaseDatabase.getInstance().reference
    }
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
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if(task.isSuccessful){
                getUserData(email)
            } else {
                loginState.value = Result.ERROR
            }
        }
    }

    private fun getUserData(email: String){
        val mailWithoutCom = email.replace(".com", "")
        firebaseDb.child("user").child(mailWithoutCom)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModelScope.launch {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            localSource.insertUser(it)
                            pref.saveIsLogin(true)
                            pref.saveUserName(it.name.orEmpty())
                        }
                        loginState.value = if(user == null) Result.ERROR else Result.SUCCESS
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("err", error.message)
                }
            })
    }
}