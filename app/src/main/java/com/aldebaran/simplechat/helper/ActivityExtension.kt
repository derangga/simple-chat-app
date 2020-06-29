package com.aldebaran.simplechat.helper

import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T: FragmentActivity> FragmentActivity.goToActivity(){
    Intent(this, T::class.java)
        .also { startActivity(it) }
}

inline fun <reified T: ViewModel> FragmentActivity.initViewModel(factory: ViewModelProvider.Factory): T{
    return ViewModelProvider(this, factory)[T::class.java]
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.visible(){
    this.visibility = View.VISIBLE
}