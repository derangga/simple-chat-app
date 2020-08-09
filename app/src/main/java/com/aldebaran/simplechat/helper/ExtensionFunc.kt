package com.aldebaran.simplechat.helper

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long?.toSimpleDateTime(): String {
    val sdf = SimpleDateFormat("HH:mm, dd MMM yyyy")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0L
    return sdf.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun Long?.toDateTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0L
    return sdf.format(calendar.time)
}