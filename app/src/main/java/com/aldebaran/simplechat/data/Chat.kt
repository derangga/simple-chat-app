package com.aldebaran.simplechat.data

data class Chat(
    val from: String? = null,
    var message: String? = null,
    var createdAt: String? = null,
    var key: String? = null
){
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "createdAt" to createdAt,
            "from" to from,
            "message" to message
        )
    }
}