package com.aldebaran.simplechat.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aldebaran.simplechat.ServicesLocator
import com.aldebaran.simplechat.R
import com.aldebaran.simplechat.databinding.ActivityLoginBinding
import com.aldebaran.simplechat.helper.*
import com.aldebaran.simplechat.ui.login.Result.*
import com.aldebaran.simplechat.ui.login.Credential.*
import com.aldebaran.simplechat.ui.room.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var factory: LoginFactory
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    private lateinit var progressDialogBuilder: AlertDialog.Builder
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initLoadingDialog()
        subscribeUI()

        if(viewModel.getLoginStatus()){
            goToActivity<MainActivity>()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.btnLogin.setOnClickListener {
            viewModel.checkCredential(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    private fun initViewModel(){
        factory = ServicesLocator.provideLoginFactory(applicationContext)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun initLoadingDialog(){
        progressDialogBuilder = AlertDialog.Builder(this)
        progressDialogBuilder.setView(R.layout.dialog_progress)
        progressDialog = progressDialogBuilder.create()
        progressDialog.setCancelable(false)
    }

    private fun showLoading(){
        progressDialog.show()
    }

    private fun hideLoading(){
        progressDialog.dismiss()
    }

    private fun subscribeUI(){
        viewModel.validator.observe(this, Observer { credential ->
            credential?.let {
                when( it ){
                    INVALID_EMAIL -> binding.etEmail.error = getString(R.string.invalid_email)
                    INVALID_PASSWORD -> binding.etPassword.error = getString(R.string.invalid_password)
                    ALL_DATA_VALID -> {
                        showLoading()
                        viewModel.doLogin(binding.etEmail.text.toString(),
                            binding.etPassword.text.toString())
                    }
                }
            }
        })

        viewModel.loginState.observe(this, Observer { result ->
            result?.let { status ->
                when(status){
                    LOADING -> { showLoading() }
                    SUCCESS -> {
                        hideLoading()
                        viewModel.saveSession(binding.etEmail.text.toString())
                        goToActivity<MainActivity>()
                        finish()

                    }
                    ERROR -> {
                        hideLoading()
                        Toast.makeText(this, "Google Login", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}