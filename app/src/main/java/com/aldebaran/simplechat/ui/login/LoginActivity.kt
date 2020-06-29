package com.aldebaran.simplechat.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.lifecycle.Observer
import com.aldebaran.simplechat.R
import com.aldebaran.simplechat.base.BaseActivity
import com.aldebaran.simplechat.databinding.ActivityLoginBinding
import com.aldebaran.simplechat.helper.*
import com.aldebaran.simplechat.helper.Result.*
import com.aldebaran.simplechat.ui.room.MainActivity
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @Inject lateinit var factory: LoginFactory
    private lateinit var viewModel: LoginViewModel
    private lateinit var mailWatcher: TextStream
    private lateinit var passwordWatcher: TextStream
    private lateinit var progressDialogBuilder: AlertDialog.Builder
    private lateinit var progressDialog: Dialog

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun onCreateScope(savedInstanceState: Bundle?) {
        getComponent().inject(this)
        viewModel = initViewModel(factory)
        progressDialogBuilder = AlertDialog.Builder(this)
        progressDialogBuilder.setView(R.layout.dialog_progress)
        progressDialog = progressDialogBuilder.create()
        progressDialog.setCancelable(false)
        initWatcher()
        subscribeUI()

        if(viewModel.getLoginStatus()){
            goToActivity<MainActivity>()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.btnLogin.setOnClickListener {
            hideErrorText()
            viewModel.validator(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    override fun onDestroy() {
        removeWatcher()
        super.onDestroy()
    }

    private fun initWatcher(){
        mailWatcher = object: TextStream(){
            override fun onWrite(text: String, isEmpty: Boolean) {
                if(isEmpty){
                    binding.tvEmailErr.visible()
                    binding.tvEmailErr.text = getString(R.string.mail_empty)
                } else binding.tvEmailErr.gone()
            }
        }

        passwordWatcher = object: TextStream(){
            override fun onWrite(text: String, isEmpty: Boolean) {
                if(isEmpty){
                    binding.tvPasswordErr.visible()
                    binding.tvPasswordErr.text = getString(R.string.password_empty)
                } else binding.tvPasswordErr.gone()
            }
        }

        binding.etEmail.addTextChangedListener(mailWatcher)
        binding.etPassword.addTextChangedListener(passwordWatcher)
    }

    private fun removeWatcher(){
        binding.etEmail.removeTextChangedListener(mailWatcher)
        binding.etPassword.removeTextChangedListener(passwordWatcher)
    }

    private fun subscribeUI(){
        viewModel.validator.observe(this, Observer { errorId ->
            when(errorId){
                0 -> {
                    binding.tvEmailErr.text = getString(R.string.invalid_email)
                    binding.tvEmailErr.visible()
                }
                1 -> {
                    binding.tvPasswordErr.text = getString(R.string.invalid_password)
                    binding.tvPasswordErr.visible()
                }
                2 -> {
                    showLoading()
                    viewModel.doLogin(binding.etEmail.text.toString(),
                        binding.etPassword.text.toString())
                }
            }
        })

        viewModel.loginState.observe(this, Observer { result ->
            when(result){
                SUCCESS -> {
                    hideLoading()
                    goToActivity<MainActivity>()
                    finish()
                }
                ERROR -> {
                    hideLoading()
                    showToast("Login Error")
                }
                else -> hideLoading()
            }
        })
    }

    private fun hideErrorText(){
        binding.tvEmailErr.gone()
        binding.tvPasswordErr.gone()
    }

    private fun showLoading(){
        progressDialog.show()
    }

    private fun hideLoading(){
        progressDialog.dismiss()
    }
}