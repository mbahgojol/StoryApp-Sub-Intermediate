package com.mbahgojol.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.mbahgojol.storyapp.data.local.SharedPref
import com.mbahgojol.storyapp.data.model.LoginBody
import com.mbahgojol.storyapp.databinding.ActivityLoginBinding
import com.mbahgojol.storyapp.ui.home.MainActivity
import com.mbahgojol.storyapp.ui.register.RegisterActivity
import com.mbahgojol.storyapp.utils.ResultState
import com.mbahgojol.storyapp.utils.text
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        if (sharedPref.isLogin) Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text()
                val password = etPassword.text()

                when {
                    email.isEmpty() -> {
                        etEmail.error = "Silahkan Isi Email Dulu"
                    }

                    password.isEmpty() -> {
                        etPassword.error = "Silahkan Isi Password Dulu"
                    }
                    else -> {
                        viewModel.login(LoginBody(email, password))
                    }
                }
            }

            btnRegister.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        viewModel.resultStateLogin.observe(this) {
            when (it) {
                is ResultState.Error -> {
                    val snackbar = Snackbar.make(
                        binding.root,
                        it.e.message.toString(),
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.show()
                }
                is ResultState.Loading -> {
                    binding.progressBar.isVisible = it.loading
                }
                is ResultState.Success<*> -> {
                    sharedPref.isLogin = true
                    Intent(this, MainActivity::class.java).apply {
                        startActivity(this)
                        finish()
                    }
                }
            }
        }
    }
}