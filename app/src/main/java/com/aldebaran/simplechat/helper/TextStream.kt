package com.aldebaran.simplechat.helper

import android.text.Editable
import android.text.TextWatcher

abstract class TextStream: TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        s?.also { text ->
            if(text.isNotEmpty()) onWrite(s.toString(), false)
            else onWrite(s.toString(), true)
        }
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    abstract fun onWrite(text: String, isEmpty: Boolean)
}