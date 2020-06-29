package com.aldebaran.simplechat.helper

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
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

@BindingAdapter("time_message")
fun setTimeMessage(view: TextView, time: Long?){
    view.text = time.toSimpleDateTime()
}

@BindingAdapter("sender")
fun setSenderName(view: TextView, name: String?){
    val firstName = name.orEmpty().split(" ")
    view.text = firstName.first()
}