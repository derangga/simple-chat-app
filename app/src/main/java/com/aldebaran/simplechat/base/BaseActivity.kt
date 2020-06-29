package com.aldebaran.simplechat.base
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.aldebaran.simplechat.MyApp

abstract class BaseActivity<B: ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayout())
        onCreateScope(savedInstanceState)
    }

    protected abstract fun getLayout(): Int
    protected abstract fun onCreateScope(savedInstanceState: Bundle?)

    protected fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun setLog(message: String){
        Log.e(this::class.java.simpleName, message)
    }

    protected fun getBundle() = intent.extras
    protected fun getComponent() = (application as MyApp).getComponent()
}