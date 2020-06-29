package com.aldebaran.simplechat.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldebaran.simplechat.database.LocalDataSource
import com.aldebaran.simplechat.database.SharedPref
import com.aldebaran.simplechat.database.table.Messages
import com.aldebaran.simplechat.helper.toDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainViewModel(
    private val localSource: LocalDataSource,
    private val pref: SharedPref
): ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firebaseRef by lazy { FirebaseDatabase.getInstance().reference }
    private lateinit var firebaseListener: ValueEventListener
    private val MESSAGE = "message"

    fun actionLogout(onClearSuccess: () -> Unit){
        viewModelScope.launch {
            auth.signOut()
            localSource.truncateMessage()
            localSource.truncateUser()
            pref.clearPref()
            onClearSuccess()
        }
    }

    fun streamDb(){
        firebaseListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch(IO) {
                    val data = snapshot.children.toMutableList().map { child ->
                        val message = child.getValue(Messages::class.java)
                        message ?: Messages()
                    }
                    localSource.insertMessage(data)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        firebaseRef.child(MESSAGE).addValueEventListener(firebaseListener)
    }

    fun sendMessage(message: String, from: String){
        val millis = System.currentTimeMillis()
        val messageObj = Messages(millis, from, message, millis.toDateTime())
        firebaseRef.child(MESSAGE).push().setValue(messageObj)
    }
    fun getUserName(): String = pref.getUserName()
    fun observerMessage() = localSource.getAllMessage()
    fun handleLeadingSpace(text: String): String{
        return text.trim().replace("^\\s+|\\s+$|^[\\n\\r]|[\\n\\r]$", "")
    }
}