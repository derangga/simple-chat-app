package com.aldebaran.simplechat.ui.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aldebaran.simplechat.data.SharedPref
import com.aldebaran.simplechat.data.Chat
import com.aldebaran.simplechat.helper.toDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: SharedPref
): ViewModel() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firebaseRef by lazy { FirebaseDatabase.getInstance().reference }
    private lateinit var firebaseListener: ValueEventListener

    private val _chat by lazy { MutableLiveData<List<Chat>>() }
    val chat: LiveData<List<Chat>>
        get() = _chat

    private val _bubbleChat by lazy { MutableLiveData<Chat>() }
    val bubbleChat: LiveData<Chat>
        get() = _bubbleChat

    private val MESSAGE = "message"

    fun actionLogout(onClearSuccess: () -> Unit){
        auth.signOut()
        pref.clearPref()
        onClearSuccess()
    }

    fun streamDb(){
        firebaseListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch(Default) {
                    val dataChat = snapshot.children.toMutableList().map { child ->
                        val message = child.getValue(Chat::class.java)
                        message?.key = child.key
                        message ?: Chat()
                    }
                    _chat.postValue(dataChat)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ViewModel", databaseError.message)
            }

        }

        firebaseRef.child(MESSAGE)
            .addValueEventListener(firebaseListener)
    }

    fun sendMessage(message: String, from: String){
        val millis = System.currentTimeMillis()
        val messageObj =
            Chat(from, message, millis.toDateTime())
        firebaseRef.child(MESSAGE).push().setValue(messageObj)
    }

    fun onBubbleChatLongClick(chat: Chat){
        _bubbleChat.value = chat
    }

    fun updateChat(chat: Chat){
        val childUpdate = hashMapOf<String, Any>(
            "/$MESSAGE/${chat.key}" to chat.toMap()
        )
        firebaseRef.updateChildren(childUpdate)
    }

    fun deleteChat(chat: Chat){
        firebaseRef.child(MESSAGE)
            .child(chat.key.orEmpty())
            .removeValue()
    }

    fun getUserName(): String = pref.getUserName()

    fun handleLeadingSpace(text: String): String{
        return text.trim().replace("^\\s+|\\s+$|^[\\n\\r]|[\\n\\r]$", "")
    }

    override fun onCleared() {
        firebaseRef.removeEventListener(firebaseListener)
        super.onCleared()
    }
}