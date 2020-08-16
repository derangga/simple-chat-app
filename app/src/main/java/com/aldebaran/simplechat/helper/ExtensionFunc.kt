package com.aldebaran.simplechat.helper

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String?.toSimpleDateTime(): String {
    return try{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val parser = sdf.parse(this.orEmpty())
        parser?.let {
            SimpleDateFormat("HH:mm, dd MMM yyyy").format(it)
        }.orEmpty()
    }catch (ex: ParseException){
        ""
    }
}

@SuppressLint("SimpleDateFormat")
fun Long?.toDateTime(): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this ?: 0L
        sdf.format(calendar.time)
    }catch (ex: ParseException){
        ""
    }
}