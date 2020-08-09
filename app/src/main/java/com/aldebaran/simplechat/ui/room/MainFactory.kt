package com.aldebaran.simplechat.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldebaran.simplechat.data.SharedPref

class MainFactory (
    private val sharedPref: SharedPref
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(SharedPref::class.java)
            .newInstance(sharedPref)
    }
}