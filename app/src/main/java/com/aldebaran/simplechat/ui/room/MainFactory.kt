package com.aldebaran.simplechat.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldebaran.simplechat.database.LocalDataSource
import com.aldebaran.simplechat.database.SharedPref
import javax.inject.Inject

class MainFactory @Inject constructor(
    private val localSource: LocalDataSource,
    private val sharedPref: SharedPref
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(LocalDataSource::class.java, SharedPref::class.java)
            .newInstance(localSource, sharedPref)
    }
}